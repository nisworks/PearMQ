package com.nis.mom.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.parser.ParseException;

import com.nis.mom.common.ErrorCode;
import com.nis.mom.common.JsonUtil;
import com.nis.mom.common.MomParams;
import com.nis.mom.common.exception.MomRuntimeException;
import com.nis.mom.common.exception.MomSocketException;

public class SocketDispatcher {

	private int socketOffset;
	private SocketServer[] socketPool;
	private HashMap<String, SocketServer> activeClients;
	private MomProcesorFactory procesorFactory;

	public SocketDispatcher(int baseSocket, int socketPoolSize,
			MomProcesorFactory procesorFactory) {
		this.socketOffset = baseSocket;
		this.socketPool = new SocketServer[socketPoolSize + 1];
		this.procesorFactory = procesorFactory;
	}
	
	public SocketDispatcher( int socketPoolSize,
			MomProcesorFactory procesorFactory) {
		this(MomParams.DEFAULT_PORT, socketPoolSize, procesorFactory);
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
						Map<MomParams, Object> request = JsonUtil.decodeMap(input.readUTF());
						System.out.println("server initate dispatcher: " + request.get(MomParams.SERVICE));
						
						Map<MomParams, Object> params = new HashMap<MomParams, Object>();
						
						if(procesorFactory==null||procesorFactory.getServiceList()==null||!procesorFactory.getServiceList().contains(request.get(MomParams.SERVICE))){
							System.out.println("unknown service: " + request.get(MomParams.SERVICE));
							params.put(MomParams.ERROR_CODE, ErrorCode.UNKNOWN_SERVICE.toString());
							
						} else {
							SocketServer server = openSocket();
							params.put(MomParams.SERVICE, request.get(MomParams.SERVICE));
							params.put(MomParams.PORT, server.getPortNumber());
							params.put(MomParams.SERVICE_LIST, procesorFactory.getServiceList());							
						}

						output.writeUTF(JsonUtil.encodeMap(params));
						

						dispatcherSocket.close();
					}

				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
		throw new MomRuntimeException("Socket pool exausted");
	}

	protected synchronized void processRequest(final MomRequest request,
			final SocketServer socket) {
		Runnable requestThread = new Runnable() {
			public void run() {
				MomProcesor processor = procesorFactory.initate(request.getService());
				request.setResponse(processor.processRequest(request.getRequest(),request));
				try {
					socket.writeResponse(request);
				} catch (MomSocketException e) {
					disposeResponse(request);
					socketPool[socket.getPortNumber() - socketOffset] = null;
					e.printStackTrace();
				}
			}
		};

		new Thread(requestThread).start();
	}

	private void disposeResponse(MomRequest request) {

	}
}
