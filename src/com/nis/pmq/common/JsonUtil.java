package com.nis.pmq.common;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonUtil {

	static private JSONParser parser = new JSONParser();

	public static String encode(PmqEnvelope payload) {
		JSONObject env = new JSONObject();
		env.put("payload", payload.getPayload());
		env.put("service", payload.getService());
		env.put("timestamp", payload.getTimestamp());
		env.put("errorCode", payload.getErrorCode());
		env.put("uuid", payload.getUuid());
		env.put("timeout", payload.getTimeout());

		return env.toJSONString();
	}

	public static String encodeMap(Map<PmqParams, Object> payload) {
		JSONObject env = new JSONObject();
		env.putAll(payload);
		return env.toJSONString();
	}

	public static PmqEnvelope decode(String payload) throws ParseException {
		PmqEnvelope result = new PmqEnvelope();
		JSONObject json = (JSONObject) parser.parse(payload);

		result.setErrorCode((String) json.get("errorCode"));
		result.setService((String) json.get("service"));
		result.setPayload((String) json.get("payload"));
		result.setTimestamp((String) json.get("timestamp"));
		result.setUuid((String) json.get("uuid"));
		result.setTimeout((Long) json.get("timeout"));

		return result;
	}

	public static Map<PmqParams, Object> decodeMap(String payload)
			throws ParseException {
		PmqEnvelope result = new PmqEnvelope();
		JSONObject json = (JSONObject) parser.parse(payload);

		Map<PmqParams, Object> resultMap = new HashMap<PmqParams, Object>();
		for (Object s : json.keySet()) {
			if (PmqParams.valueOf(s.toString()) != null) {
				resultMap.put(PmqParams.valueOf(s.toString()), json.get(s));
			}
		}
		return resultMap;
	}
}
