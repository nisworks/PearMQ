package com.nis.mom.client.loadbalancer;

import java.util.Set;

import com.nis.mom.client.SocketClient;

public class RoundRobinStrategy implements LoadBalancerStrategy {

	@Override
	public SocketClient chooseServer(ServiceStatsData socketStatsData) {

		Set<SocketClient> set = socketStatsData.getSocketSet();
		return set.iterator().next();
	}



}
