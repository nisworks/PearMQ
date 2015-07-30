package com.nis.mom.test;

import java.util.ArrayList;
import java.util.List;

import com.nis.mom.server.MomProcesor;
import com.nis.mom.server.MomProcesorFactory;
import com.nis.mom.server.MomRequest;

public class ProcessorFactoryMock implements MomProcesorFactory {

	@Override
	public MomProcesor initate(String service) {
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
	
	public class MomProcesorMock implements MomProcesor{

		@Override
		public String processRequest(String payload, MomRequest request) {
			// TODO Auto-generated method stub
			return payload+" pong";
		}
		
	}

}
