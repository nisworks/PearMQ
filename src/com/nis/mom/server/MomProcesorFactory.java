package com.nis.mom.server;

import java.util.List;



public interface MomProcesorFactory {

	public MomProcesor initate(String service);
	public List<String> getServiceList();
}
