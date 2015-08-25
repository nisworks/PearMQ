package com.nis.pmq.client;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.nis.pmq.client.loadbalancer.LoadBalancerStrategy;
import com.nis.pmq.common.EmptyPersister;
import com.nis.pmq.common.PmqEnvelope;
import com.nis.pmq.common.PmqParams;
import com.nis.pmq.common.PmqPersister;
import com.nis.pmq.common.exception.PmqServiceException;
import com.nis.pmq.common.exception.PmqSocketException;

public class ServiceDispatcher {
	
	private Map<String, ServiceMetadata> services = new ConcurrentHashMap<String,  ServiceMetadata>();
	private Map<String, RequestThread> requestCallbacks = new ConcurrentHashMap<String, RequestThread>();
	private PmqPersister persister = new EmptyPersister();

	
	public ClientRequest callService(String service, String request, long timeout) throws PmqServiceException{

		ClientRequest pmqRequest = new ClientRequest(service, request, timeout);
		
		return callService(pmqRequest);
	}

	public ClientRequest callService( ClientRequest pmqRequest)
			throws PmqServiceException {
		ServiceMetadata connectorStrategy = services.get(pmqRequest.getService());
		SocketClient serverConnector = connectorStrategy.getSocket(pmqRequest);
		
		
		final Thread currentThread = Thread.currentThread();
		requestCallbacks.put(pmqRequest.getUuid(), new RequestThread(currentThread, pmqRequest));
		
		
		try {
			serverConnector.callService(pmqRequest);
		} catch (PmqSocketException e1) {
			throw new PmqServiceException(e1);
		}
		try {
			currentThread.sleep(pmqRequest.getTimeout());
		} catch (InterruptedException e) {
			return pmqRequest;
		}
		throw new PmqServiceException("Service timeout: "+pmqRequest.getService());
	}
	
	public void setServiceStrategy(String service, LoadBalancerStrategy strategy){
		ServiceMetadata connectorStrategy = services.get(service);
		connectorStrategy.setStrategy(strategy);
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
			 ServiceMetadata socketData = services.get(s);
			if(socketData==null){
				socketData = new ServiceMetadata(s);
				services.put(s, socketData);
			}
			
			socketData.registerSocket(socket);			
		}
	}
	
	private class RequestThread{
		
		private Thread thread;
		private ClientRequest request;
		
		
		
		public RequestThread(Thread thread, ClientRequest request) {
			super();
			this.thread = thread;
			this.request = request;
		}
		public Thread getThread() {
			return thread;
		}
		public ClientRequest getRequest() {
			return request;
		}
		
		
	}
	
	public PmqPersister getPersister() {
		return persister;
	}

	public void setPersister(PmqPersister persister) {
		this.persister = persister;
	}

}
