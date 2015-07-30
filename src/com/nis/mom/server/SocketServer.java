package com.nis.mom.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import org.json.simple.parser.ParseException;

import com.nis.mom.common.JsonUtil;
import com.nis.mom.common.MomEnvelope;
import com.nis.mom.common.exception.MomRuntimeException;
import com.nis.mom.common.exception.MomSocketException;

public class SocketServer {

	private int portNumber;
	private ServerSocket socketService;
	private Socket socket;
	private SocketDispatcher socketDispatcher;
	private DataInputStream input;
	private DataOutputStream output;
	private String clientId;

	public SocketServer(int portNumber, SocketDispatcher socketDispatcher) {
		super();
		this.portNumber = portNumber;
		this.socketDispatcher = socketDispatcher;
	}

	public void openSocket() {
		new Thread(new Runnable() {
			public void run() {
				try {
					socketService = new ServerSocket(portNumber);
					socketService.setSoTimeout(60000);
					System.out.println("socket open: " + portNumber);
					try {
						socket = socketService.accept();

					} catch (java.io.InterruptedIOException e) {
						System.err.println("Timed Out (60 sec)!");
						socketService.close();
						return;
					}

					input = new DataInputStream(socket.getInputStream());
					output = new DataOutputStream(socket.getOutputStream());

					System.out.println("Connected: "+portNumber);
					
					String requestString;
					clientId = socket.getInetAddress().getHostAddress();

					while ((requestString = input.readUTF()) != null) {
						System.out.println("Server read: " + requestString);
						MomEnvelope envelope;
						try {
							envelope = JsonUtil.decode(requestString);
							MomRequest request = new MomRequest(
									envelope.getPayload(), clientId,
									envelope.getService(), envelope.getUuid());
							socketDispatcher.processRequest(request, SocketServer.this);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				} catch (IOException e) {
					System.out.println(e);
				}
			}
		}).start();
	}

	public synchronized void writeResponse(MomRequest request)
			throws MomSocketException {
		if (isClosed()) {
			throw new MomSocketException("Socket is closed");
		}
		if (!isClosed() && clientId.equals(request.getClientId())) {
			try {
				MomEnvelope envelope = new MomEnvelope();
				envelope.setPayload(request.getResponse());
				envelope.setTimestamp(new Date().toString());
				envelope.setService(request.getService());
				envelope.setUuid(request.getUuid());
				output.writeUTF(JsonUtil.encode(envelope));
				return;
			} catch (IOException e) {
				throw new MomSocketException(e);
			}
		}
		throw new MomSocketException("Illegal clientId");
	}

	public boolean isClosed() {
		return socketService.isClosed();
	}

	public int getPortNumber() {
		return portNumber;
	}

	public String getClientId() {
		return clientId;
	}

}
