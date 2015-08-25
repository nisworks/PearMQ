package com.nis.pmq.client.loadbalancer;

import java.util.Map;

import com.nis.pmq.client.ClientRequest;
import com.nis.pmq.client.ServiceMetadata;
import com.nis.pmq.client.SocketClient;

public interface LoadBalancerStrategy {

	public SocketClient chooseServer(ClientRequest ClientRequest, ServiceMetadata socketStatsData);
	
	public void registerSocket(SocketClient socket);
	
	public void removeSocket(SocketClient socket);
}
