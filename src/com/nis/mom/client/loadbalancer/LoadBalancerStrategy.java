package com.nis.mom.client.loadbalancer;

import java.util.Map;

import com.nis.mom.client.SocketClient;

public interface LoadBalancerStrategy {

	public SocketClient chooseServer(ServiceStatsData socketStatsData);
}
