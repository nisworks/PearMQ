package com.nis.mom.test;

import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import com.nis.mom.common.JsonUtil;
import com.nis.mom.common.MomEnvelope;

public class JsonTest {
	
	JSONParser parser=new JSONParser();

	@Test
	public void test() throws ParseException {
		  JSONObject obj=new JSONObject();
		  obj.put("name","foo");
		  obj.put("num",new Integer(100));
		  obj.put("balance",new Double(1000.21));
		  obj.put("is_vip",new Boolean(true));
		  obj.put("nickname",null);
		  String payload = obj.toJSONString();
		  System.out.println(payload);
		  
		  JSONObject env=new JSONObject();
		  env.put("service", "MoMservice");
		  env.put("customer", "127.0.0.1");
		  env.put("payload", payload);
		  
		  String envelope = env.toJSONString();
		  
		  System.out.println(envelope);
		  
		  JSONObject result =  (JSONObject) parser.parse(envelope);
		  System.out.println(result.get("service"));
		  System.out.println(result.get("customer"));
		  System.out.println(result.get("payload"));
	}
	
	@Test
	public void test2() throws ParseException {
		JSONObject obj=new JSONObject();
		  obj.put("name","foo");
		  obj.put("num",new Integer(100));
		  obj.put("balance",new Double(1000.21));
		  obj.put("is_vip",new Boolean(true));
		  obj.put("nickname",null);
		  String payload = obj.toJSONString();
		  
		  MomEnvelope envelope = new MomEnvelope();
		  envelope.setPayload(payload);
		  envelope.setTimestamp(new Date().toString());
		  envelope.setService("jakas_usluga");
		  
		  String json = JsonUtil.encode(envelope);
		  
		  System.out.println(json);
		  
		  MomEnvelope result = JsonUtil.decode(json);
		  System.out.println(result.getErrorCode());
		  System.out.println(result.getTimestamp());
		  System.out.println(result.getService());
		  System.out.println(result.getPayload());
	}

}
