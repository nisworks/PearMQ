package com.nis.pmq.test;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Test;

import com.nis.pmq.common.ActivityLog;
import com.nis.pmq.common.PmqEnvelope;
import com.nis.pmq.common.PmqPersister;

public class PersisterTest {

	@Test
	public void test() {
		PmqPersister persister = new ActivityLog("C:/Users/b0640700/temp/");
		
		long nano = System.currentTimeMillis();
		
		String payload = "¿œ¹æó³¯ŒÆÓ£Ññiuhfu sduifhd hdfhsgi hdfgh idfshg \n	i ";
		System.out.println(payload);
		for(int i=0; i<100; i++){
			PmqEnvelope envelope = new PmqEnvelope();
			envelope.setPayload(payload+i);
			envelope.setService("testowa");
			envelope.setUuid(UUID.randomUUID().toString());
			persister.persistAll(envelope);
			persister.persistError(envelope);
		}
		System.out.println("persisted in "+(System.currentTimeMillis()-nano));
		
	}

}
