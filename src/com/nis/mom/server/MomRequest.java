package com.nis.mom.server;

public class MomRequest {

	private String request;
	private String clientId;
	private String uuid;
	private String service;
	private String response;

	public MomRequest(String request, String clientId, String service, String uuid) {
		super();
		this.request = request;
		this.clientId = clientId;
		this.service = service;
		this.uuid = uuid;
	}

	public String getRequest() {
		return request;
	}

	public String getClientId() {
		return clientId;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getService() {
		return service;
	}

	public String getUuid() {
		return uuid;
	}

	
	
}
