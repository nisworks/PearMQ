package com.nis.mom.client;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.nis.mom.client.loadbalancer.ServiceStatsData;
import com.nis.mom.common.MomEnvelope;
import com.nis.mom.common.MomParams;
import com.nis.mom.common.exception.MomServiceException;
import com.nis.mom.common.exception.MomSocketException;

public class ServiceDispatcher {
	
	private Map<String, ServiceStatsData> services = new ConcurrentHashMap<String,  ServiceStatsData>();
	private Map<String, RequestThread> requestCallbacks = new ConcurrentHashMap<String, RequestThread>();

	
	public MomRequest2 callService(String service, String request, long timeout) throws MomServiceException{
		ServiceStatsData connectorStrategy = services.get(service);
		SocketClient serverConnector = connectorStrategy.getSocket();
		String uuid = UUID.randomUUID().toString();
		MomRequest2 momRequest = new MomRequest2(uuid, service, request);
		final Thread currentThread = Thread.currentThread();
		requestCallbacks.put(uuid, new RequestThread(currentThread, momRequest));
		try {
			serverConnector.callService(momRequest, timeout);
		} catch (MomSocketException e1) {
			throw new MomServiceException(e1);
		}
		try {
			currentThread.sleep(timeout);
		} catch (InterruptedException e) {
			return momRequest;
		}
		throw new MomServiceException("Service timeout: "+service);
	}
	
	protected void processResponse(MomEnvelope envelope){
		System.out.println("client: "+envelope.getUuid());
		RequestThread requestThread = requestCallbacks.get(envelope.getUuid());
		//System.out.println(requestThread);
		requestThread.getRequest().setResponse(envelope.getPayload());
		requestThread.getThread().interrupt();
	}
	
	public void connectService(String service, String hostname, int port) throws MomSocketException{
		SocketClient client = new SocketClient(hostname, port,  this);
		client.openSocket(service);
	}
	
	public void connectService(String service, String hostname) throws MomSocketException{
		connectService(service, hostname, MomParams.DEFAULT_PORT);
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
		private MomRequest2 request;
		
		
		
		public RequestThread(Thread thread, MomRequest2 request) {
			super();
			this.thread = thread;
			this.request = request;
		}
		public Thread getThread() {
			return thread;
		}
		public MomRequest2 getRequest() {
			return request;
		}
		
		
	}

}
