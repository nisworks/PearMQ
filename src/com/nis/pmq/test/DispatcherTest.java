package com.nis.pmq.test;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.nis.pmq.client.ServiceDispatcher;
import com.nis.pmq.common.ActivityLog;
import com.nis.pmq.common.exception.PmqServiceException;
import com.nis.pmq.common.exception.PmqSocketException;
import com.nis.pmq.server.SocketDispatcher;

public class DispatcherTest {

	@Before
	public void setup(){
//		SocketDispatcher dispatcher = new SocketDispatcher(5, new ProcessorFactoryMock());
//		dispatcher.initiateDispatcher();
//		dispatcher.setPersister(new ActivityLog("C:/Users/b0640700/temp/server/"));
	}
	
	public void test() throws PmqSocketException {
				
		SocketDispatcher dispatcher = new SocketDispatcher(5, new ProcessorFactoryMock());
		dispatcher.initiateDispatcher();
		
		
		ServiceDispatcher serviceDispatcher = new ServiceDispatcher();
		
		serviceDispatcher.connectService("testService1", "localhost");
		
		
		try {
			serviceDispatcher.connectService("testService3", "localhost");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		serviceDispatcher.connectService("testService2", "localhost");
		

	}
	
	@Test
	public void test2() throws PmqSocketException, PmqServiceException{

		
		final ServiceDispatcher serviceDispatcher = new ServiceDispatcher();		
		serviceDispatcher.connectService("testService1", "localhost");
		serviceDispatcher.setPersister(new ActivityLog("C:/Users/b0640700/temp/client/", true));
		
		for(int i=0; i<1000; i++){
			long time = System.currentTimeMillis();
			serviceDispatcher.callService("testService1", i+"ping", 1000);
			System.out.println("log time: "+(System.currentTimeMillis()-time));
		}
		
		
	}

}
