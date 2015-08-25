package com.nis.pmq.client;

import java.util.HashSet;
import java.util.Set;

import com.nis.pmq.client.loadbalancer.LoadBalancerStrategy;
import com.nis.pmq.client.loadbalancer.RoundRobinStrategy;

public class ServiceMetadata {

	private String service;
	private LoadBalancerStrategy strategy;
	private Set<SocketClient> socketSet = new HashSet<SocketClient>();
	
	

	public ServiceMetadata(String service) {
		super();
		this.service = service;
		strategy = new RoundRobinStrategy();
	}



	public void registerSocket(SocketClient socket) {
		if(!socketSet.contains(socket)){
			socketSet.add(socket);
			strategy.registerSocket(socket);
			System.out.println("add socket for service: "+service);
		} else {
			System.out.println("add socket for service: "+service+" already exists");
		}
		
	}

	
	public SocketClient getSocket(ClientRequest clientRequest){
		return strategy.chooseServer(clientRequest, this);
	}

	public void setStrategy(LoadBalancerStrategy strategy) {
		this.strategy = strategy;
		for(SocketClient s : socketSet){
			strategy.registerSocket(s);
		}
	}

	
}