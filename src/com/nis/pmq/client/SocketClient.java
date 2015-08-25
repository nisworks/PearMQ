package com.nis.pmq.client;

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

import com.nis.pmq.common.JsonUtil;
import com.nis.pmq.common.PmqEnvelope;
import com.nis.pmq.common.PmqParams;
import com.nis.pmq.common.exception.PmqRuntimeException;
import com.nis.pmq.common.exception.PmqSocketException;
import com.nis.pmq.server.ServiceRequest;
import com.nis.pmq.server.SocketServer;

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

	public void openSocket(String service) throws PmqSocketException {

		Socket clientSocket;

		DataInputStream inputConnector;
		DataOutputStream outputConnector;
		try {
			Map<PmqParams, Object> params = negotiateConnection(service);

			if (params.get(PmqParams.ERROR_CODE) != null) {
				throw new PmqSocketException(
						(String) params.get(PmqParams.ERROR_CODE));
			} else {
				openReceiverSocket(params);
			}

		} catch (UnknownHostException e) {
			throw new PmqSocketException(e);
		} catch (IOException e) {
			throw new PmqSocketException(e);
		} catch (ParseException e) {
			throw new PmqSocketException(e);
		}

	}

	public synchronized void callService(ClientRequest request) throws PmqSocketException {
		PmqEnvelope envelope = new PmqEnvelope();
		envelope.setService(request.getService());
		envelope.setPayload(request.getRequest());
		envelope.setTimestamp(new Date().toString());
		envelope.setUuid(request.getUuid());
		envelope.setTimeout(request.getTimeout());
		try {
			serviceDispatcher.getPersister().persistAll(envelope);
			System.out.println("client: "+JsonUtil.encode(envelope));
			output.writeUTF(JsonUtil.encode(envelope));
		} catch (IOException e) {
			serviceDispatcher.getPersister().persistError(envelope);
			throw new PmqSocketException(e);
		}
	}

	private Map<PmqParams, Object> negotiateConnection(String service)
			throws UnknownHostException, IOException, ParseException {
		Socket clientSocket;
		DataInputStream inputConnector;
		DataOutputStream outputConnector;
		clientSocket = new Socket(hostName, portNumber);
		inputConnector = new DataInputStream(clientSocket.getInputStream());
		outputConnector = new DataOutputStream(clientSocket.getOutputStream());

		Map<PmqParams, Object> params = new HashMap<PmqParams, Object>();
		params.put(PmqParams.SERVICE, service);
		outputConnector.writeUTF(JsonUtil.encodeMap(params));

		String responseLine = inputConnector.readUTF();
		System.out.println(responseLine);
		clientSocket.close();

		params = JsonUtil.decodeMap(responseLine);
		return params;
	}

	private void openReceiverSocket(Map<PmqParams, Object> params)
			throws UnknownHostException, IOException {
		Socket clientSocket;
		clientSocket = new Socket(hostName,
				(int) (long) params.get(PmqParams.PORT));
		input = new DataInputStream(clientSocket.getInputStream());
		output = new DataOutputStream(clientSocket.getOutputStream());

		serviceDispatcher.registerService(this,
				(List<String>) params.get(PmqParams.SERVICE_LIST));

		new Thread(new Runnable() {
			public void run() {
				String requestString;
				try {
					while ((requestString = input.readUTF()) != null) {
						System.out.println("client receiver socket: " + requestString);
						PmqEnvelope envelope;

						envelope = JsonUtil.decode(requestString);
						serviceDispatcher.processResponse(envelope);

					}
				} catch (IOException e) {
					throw new PmqRuntimeException(e);
				} catch (ParseException e) {
					throw new PmqRuntimeException(e);
				}
			}
		}).start();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((hostName == null) ? 0 : hostName.hashCode());
		result = prime * result + portNumber;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SocketClient other = (SocketClient) obj;
		if (hostName == null) {
			if (other.hostName != null)
				return false;
		} else if (!hostName.equals(other.hostName))
			return false;
		if (portNumber != other.portNumber)
			return false;
		return true;
	}
	
	
}
