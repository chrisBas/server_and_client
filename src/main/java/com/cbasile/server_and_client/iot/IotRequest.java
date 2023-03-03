package com.cbasile.server_and_client.iot;

import java.util.Map;

public class IotRequest {
	
	private String message;
	private Map<String, String> params;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

}
