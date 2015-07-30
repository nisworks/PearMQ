package com.nis.pmq.client;

public class PmqRequest2 {

	private String request;
	private String uuid;
	private String service;
	private String response;
	
	public PmqRequest2(String uuid, String service, String request) {
		super();
		this.uuid = uuid;
		this.service = service;
		this.request = request;
	}

	public String getRequest() {
		return request;
	}

	public String getUuid() {
		return uuid;
	}

	public String getService() {
		return service;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
	
	
}
