package com.nis.pmq.client;

import java.util.UUID;

public class ClientRequest {

	private String request;
	private String uuid;
	private String service;
	private String response;
	private String dimension;
	private long timeout;
	
	public ClientRequest(String service, String request, long timeout) {
		this(service,request,null, timeout);
	}
	
	public ClientRequest(String service, String request, String dimension, long timeout) {
		super();
		this.uuid = UUID.randomUUID().toString();
		this.service = service;
		this.request = request;
		this.timeout = timeout;
		this.dimension = dimension;
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

	public String getDimension() {
		return dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public long getTimeout() {
		return timeout;
	}
	
	
	
}
