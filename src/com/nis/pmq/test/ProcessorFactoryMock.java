package com.nis.pmq.test;

import java.util.ArrayList;
import java.util.List;

import com.nis.pmq.server.PmqProcesor;
import com.nis.pmq.server.PmqProcesorFactory;
import com.nis.pmq.server.ServiceRequest;

public class ProcessorFactoryMock implements PmqProcesorFactory {

	@Override
	public PmqProcesor initate(String service) {
		// TODO Auto-generated method stub
		return new MomProcesorMock();
	}

	@Override
	public List<String> getServiceList() {
		List<String> result = new ArrayList<String>();
		result.add("testService1");
		result.add("testService2");
		result.add("testService4");
		return result;
	}
	
	public class MomProcesorMock implements PmqProcesor{

		@Override
		public String processRequest(String payload, ServiceRequest request) {
			// TODO Auto-generated method stub
			return payload+" pong";
		}
		
	}

}
