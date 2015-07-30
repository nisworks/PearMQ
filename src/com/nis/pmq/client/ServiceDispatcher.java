package com.nis.pmq.client;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.nis.pmq.client.loadbalancer.ServiceStatsData;
import com.nis.pmq.common.PmqEnvelope;
import com.nis.pmq.common.PmqParams;
import com.nis.pmq.common.exception.PmqServiceException;
import com.nis.pmq.common.exception.PmqSocketException;

public class ServiceDispatcher {
	
	private Map<String, ServiceStatsData> services = new ConcurrentHashMap<String,  ServiceStatsData>();
	private Map<String, RequestThread> requestCallbacks = new ConcurrentHashMap<String, RequestThread>();

	
	public PmqRequest2 callService(String service, String request, long timeout) throws PmqServiceException{
		ServiceStatsData connectorStrategy = services.get(service);
		SocketClient serverConnector = connectorStrategy.getSocket();
		String uuid = UUID.randomUUID().toString();
		PmqRequest2 momRequest = new PmqRequest2(uuid, service, request);
		final Thread currentThread = Thread.currentThread();
		requestCallbacks.put(uuid, new RequestThread(currentThread, momRequest));
		try {
			serverConnector.callService(momRequest, timeout);
		} catch (PmqSocketException e1) {
			throw new PmqServiceException(e1);
		}
		try {
			currentThread.sleep(timeout);
		} catch (InterruptedException e) {
			return momRequest;
		}
		throw new PmqServiceException("Service timeout: "+service);
	}
	
	protected void processResponse(PmqEnvelope envelope){
		System.out.println("client: "+envelope.getUuid());
		RequestThread requestThread = requestCallbacks.get(envelope.getUuid());
		//System.out.println(requestThread);
		requestThread.getRequest().setResponse(envelope.getPayload());
		requestThread.getThread().interrupt();
	}
	
	public void connectService(String service, String hostname, int port) throws PmqSocketException{
		SocketClient client = new SocketClient(hostname, port,  this);
		client.openSocket(service);
	}
	
	public void connectService(String service, String hostname) throws PmqSocketException{
		connectService(service, hostname, PmqParams.DEFAULT_PORT);
	}
	
	protected synchronized void registerService(SocketClient socket, List<String> serviceList){
		for(String s : serviceList){
			 ServiceStatsData socketData = services.get(s);
			if(socketData==null){
				socketData = new ServiceStatsData(s);
				services.put(s, socketData);
			}
			
			socketData.registerSocket(socket);			
		}
	}
	
	private class RequestThread{
		
		private Thread thread;
		private PmqRequest2 request;
		
		
		
		public RequestThread(Thread thread, PmqRequest2 request) {
			super();
			this.thread = thread;
			this.request = request;
		}
		public Thread getThread() {
			return thread;
		}
		public PmqRequest2 getRequest() {
			return request;
		}
		
		
	}

}
