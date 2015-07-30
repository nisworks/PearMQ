package com.nis.mom.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.simple.parser.ParseException;

import com.nis.mom.common.JsonUtil;
import com.nis.mom.common.MomEnvelope;
import com.nis.mom.common.MomParams;
import com.nis.mom.common.exception.MomSocketException;
import com.nis.mom.server.MomRequest;
import com.nis.mom.server.SocketServer;

public class SocketClient {

	private String hostName;
	private int portNumber;
	private ServiceDispatcher serviceDispatcher;
	private DataInputStream input;
	private DataOutputStream output;

	public SocketClient(String hostName, int portNumber, 
			ServiceDispatcher serviceDispatcher) {
		super();
		this.hostName = hostName;
		this.portNumber = portNumber;
		this.serviceDispatcher = serviceDispatcher;
	}

	public void openSocket(String service) throws MomSocketException {

		Socket clientSocket;

		DataInputStream inputConnector;
		DataOutputStream outputConnector;
		try {
			Map<MomParams, Object> params = negotiateConnection(service);

			if (params.get(MomParams.ERROR_CODE) != null) {
				throw new MomSocketException(
						(String) params.get(MomParams.ERROR_CODE));
			} else {
				openReceiverSocket(params);
			}

		} catch (UnknownHostException e) {
			throw new MomSocketException(e);
		} catch (IOException e) {
			throw new MomSocketException(e);
		} catch (ParseException e) {
			throw new MomSocketException(e);
		}

	}

	public synchronized void callService(MomRequest2 request, long timeout) throws MomSocketException {
		MomEnvelope envelope = new MomEnvelope();
		envelope.setService(request.getService());
		envelope.setPayload(request.getRequest());
		envelope.setTimestamp(new Date().toString());
		envelope.setUuid(request.getUuid());
		envelope.setTimeout(timeout);
		try {
			System.out.println("client: "+JsonUtil.encode(envelope));
			output.writeUTF(JsonUtil.encode(envelope));
		} catch (IOException e) {
			throw new MomSocketException(e);
		}
	}

	private Map<MomParams, Object> negotiateConnection(String service)
			throws UnknownHostException, IOException, ParseException {
		Socket clientSocket;
		DataInputStream inputConnector;
		DataOutputStream outputConnector;
		clientSocket = new Socket(hostName, portNumber);
		inputConnector = new DataInputStream(clientSocket.getInputStream());
		outputConnector = new DataOutputStream(clientSocket.getOutputStream());

		Map<MomParams, Object> params = new HashMap<MomParams, Object>();
		params.put(MomParams.SERVICE, service);
		outputConnector.writeUTF(JsonUtil.encodeMap(params));

		String responseLine = inputConnector.readUTF();
		System.out.println(responseLine);
		clientSocket.close();

		params = JsonUtil.decodeMap(responseLine);
		return params;
	}

	private void openReceiverSocket(Map<MomParams, Object> params)
			throws UnknownHostException, IOException {
		Socket clientSocket;
		clientSocket = new Socket(hostName,
				(int) (long) params.get(MomParams.PORT));
		input = new DataInputStream(clientSocket.getInputStream());
		output = new DataOutputStream(clientSocket.getOutputStream());

		serviceDispatcher.registerService(this,
				(List<String>) params.get(MomParams.SERVICE_LIST));

		new Thread(new Runnable() {
			public void run() {
				String requestString;
				try {
					while ((requestString = input.readUTF()) != null) {
						System.out.println("client receiver socket: " + requestString);
						MomEnvelope envelope;

						envelope = JsonUtil.decode(requestString);
						serviceDispatcher.processResponse(envelope);

					}
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
}
