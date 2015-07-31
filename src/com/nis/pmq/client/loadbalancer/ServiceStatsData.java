package com.nis.pmq.client.loadbalancer;

import java.util.HashSet;
import java.util.Set;

import com.nis.pmq.client.SocketClient;

public class ServiceStatsData {

	private String service;
	private LoadBalancerStrategy strategy;
	
	

	public ServiceStatsData(String service) {
		super();
		this.service = service;
		strategy = new RoundRobinStrategy();
	}



	public void registerSocket(SocketClient socket) {
		strategy.registerSocket(socket);
		System.out.println("add socket for service: "+service);
	}

	
	public SocketClient getSocket(){
		return strategy.chooseServer(this);
	}

}