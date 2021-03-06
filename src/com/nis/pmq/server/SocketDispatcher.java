package com.nis.pmq.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.parser.ParseException;

import com.nis.pmq.common.EmptyPersister;
import com.nis.pmq.common.ErrorCode;
import com.nis.pmq.common.JsonUtil;
import com.nis.pmq.common.PmqParams;
import com.nis.pmq.common.PmqPersister;
import com.nis.pmq.common.exception.PmqRuntimeException;
import com.nis.pmq.common.exception.PmqSocketException;

public class SocketDispatcher {

	private int socketOffset;
	private SocketServer[] socketPool;
	private HashMap<String, SocketServer> activeClients;
	private PmqProcesorFactory procesorFactory;
	private PmqPersister persister = new EmptyPersister();

	public SocketDispatcher(int baseSocket, int socketPoolSize,
			PmqProcesorFactory procesorFactory) {
		this.socketOffset = baseSocket;
		this.socketPool = new SocketServer[socketPoolSize + 1];
		this.procesorFactory = procesorFactory;
	}
	
	public SocketDispatcher( int socketPoolSize,
			PmqProcesorFactory procesorFactory) {
		this(PmqParams.DEFAULT_PORT, socketPoolSize, procesorFactory);
	}

	public void initiateDispatcher() {

		new Thread(new Runnable() {
			public void run() {
				Socket dispatcherSocket;
				DataInputStream input;
				DataOutputStream output;
				try {
					ServerSocket socketService = new ServerSocket(socketOffset);
					while (true) {
						dispatcherSocket = socketService.accept();
						input = new DataInputStream(dispatcherSocket
								.getInputStream());
						output = new DataOutputStream(dispatcherSocket
								.getOutputStream());
						Map<PmqParams, Object> request = JsonUtil.decodeMap(input.readUTF());
						System.out.println("server initate dispatcher: " + request.get(PmqParams.SERVICE));
						
						Map<PmqParams, Object> params = new HashMap<PmqParams, Object>();
						
						if(procesorFactory==null||procesorFactory.getServiceList()==null||!procesorFactory.getServiceList().contains(request.get(PmqParams.SERVICE))){
							System.out.println("unknown service: " + request.get(PmqParams.SERVICE));
							params.put(PmqParams.ERROR_CODE, ErrorCode.UNKNOWN_SERVICE.toString());
							
						} else {
							SocketServer server = openSocket();
							params.put(PmqParams.SERVICE, request.get(PmqParams.SERVICE));
							params.put(PmqParams.PORT, server.getPortNumber());
							params.put(PmqParams.SERVICE_LIST, procesorFactory.getServiceList());							
						}

						output.writeUTF(JsonUtil.encodeMap(params));
						

						dispatcherSocket.close();
					}

				} catch (UnknownHostException e) {
					throw new PmqRuntimeException(e);
				} catch (IOException e) {
					throw new PmqRuntimeException(e);
				} catch (ParseException e) {
					throw new PmqRuntimeException(e);
				}
			}
		}).start();

	}

	private synchronized SocketServer openSocket() {
		for (int i = 1; i < socketPool.length; i++) {
			final int portNumber = socketOffset + i;
			if (socketPool[i] == null || socketPool[i].isClosed()) {
				SocketServer server = new SocketServer(portNumber,
						SocketDispatcher.this);
				socketPool[i] = server;
				server.openSocket();
				return server;
			}
		}
		throw new PmqRuntimeException("Socket pool exausted");
	}

	protected synchronized void processRequest(final ServiceRequest request,
			final SocketServer socket) {
		Runnable requestThread = new Runnable() {
			public void run() {
				PmqProcesor processor = procesorFactory.initate(request.getService());
				request.setResponse(processor.processRequest(request.getRequest(),request));
				try {
					socket.writeResponse(request);
				} catch (PmqSocketException e) {
					socketPool[socket.getPortNumber() - socketOffset] = null;
					e.printStackTrace();
				}
			}
		};

		new Thread(requestThread).start();
	}

	public PmqPersister getPersister() {
		return persister;
	}

	public void setPersister(PmqPersister persister) {
		this.persister = persister;
	}
	
	
}
