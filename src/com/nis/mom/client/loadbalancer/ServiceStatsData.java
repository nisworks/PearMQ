package com.nis.mom.client.loadbalancer;

import java.util.HashSet;
import java.util.Set;

import com.nis.mom.client.SocketClient;

public class ServiceStatsData {
	private Set<SocketClient> socketSet = new HashSet();
	private String service;
	private LoadBalancerStrategy strategy;
	
	

	public ServiceStatsData(String service) {
		super();
		this.service = service;
		strategy = new RoundRobinStrategy();
	}



	public synchronized void registerSocket(SocketClient socket) {
		if (!socketSet.contains(socket)) {
			socketSet.add(socket);
			System.out.println("add socket for service: "+service);
		}
	}



	public Set<SocketClient> getSocketSet() {
		return socketSet;
	}
	
	public SocketClient getSocket(){
		return strategy.chooseServer(this);
	}

}