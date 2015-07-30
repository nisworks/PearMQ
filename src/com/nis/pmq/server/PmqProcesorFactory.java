package com.nis.pmq.server;

import java.util.List;



public interface PmqProcesorFactory {

	public PmqProcesor initate(String service);
	public List<String> getServiceList();
}
