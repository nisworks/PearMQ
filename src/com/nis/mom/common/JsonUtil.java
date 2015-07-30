package com.nis.mom.common;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonUtil {

	static private JSONParser parser = new JSONParser();

	public static String encode(MomEnvelope payload) {
		JSONObject env = new JSONObject();
		env.put("payload", payload.getPayload());
		env.put("service", payload.getService());
		env.put("timestamp", payload.getTimestamp());
		env.put("errorCode", payload.getErrorCode());
		env.put("uuid", payload.getUuid());
		env.put("timeout", payload.getTimeout());

		return env.toJSONString();
	}

	public static String encodeMap(Map<MomParams, Object> payload) {
		JSONObject env = new JSONObject();
		env.putAll(payload);
		return env.toJSONString();
	}

	public static MomEnvelope decode(String payload) throws ParseException {
		MomEnvelope result = new MomEnvelope();
		JSONObject json = (JSONObject) parser.parse(payload);

		result.setErrorCode((String) json.get("errorCode"));
		result.setService((String) json.get("service"));
		result.setPayload((String) json.get("payload"));
		result.setTimestamp((String) json.get("timestamp"));
		result.setUuid((String) json.get("uuid"));
		result.setTimeout((Long) json.get("timeout"));

		return result;
	}

	public static Map<MomParams, Object> decodeMap(String payload)
			throws ParseException {
		MomEnvelope result = new MomEnvelope();
		JSONObject json = (JSONObject) parser.parse(payload);

		Map<MomParams, Object> resultMap = new HashMap<MomParams, Object>();
		for (Object s : json.keySet()) {
			if (MomParams.valueOf(s.toString()) != null) {
				resultMap.put(MomParams.valueOf(s.toString()), json.get(s));
			}
		}
		return resultMap;
	}
}
