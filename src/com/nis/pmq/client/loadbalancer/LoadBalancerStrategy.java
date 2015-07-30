package com.nis.pmq.client.loadbalancer;

import java.util.Map;

import com.nis.pmq.client.SocketClient;

public interface LoadBalancerStrategy {

	public SocketClient chooseServer(ServiceStatsData socketStatsData);
}
