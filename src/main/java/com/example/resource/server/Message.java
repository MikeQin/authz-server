package com.example.resource.server;

import lombok.Data;

@Data
public class Message {
	
	public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
	public static final String REFRESH_TOKEN = "REFRESH_TOKEN";
	public static final String STATUS_OK = "200 OK";
	public static final String STATUS_500 = "500 Internal Server Error";
	public static final String ACTION_REVOKE = "REVOKE";

	private String tokenType;
	private String tokenValue;
	private String action;
	private String status;
	private String error = "";
	
	
	public Message(String tokenType, String tokenValue, String action) {
		this.tokenType = tokenType;
		this.tokenValue = tokenValue;
		this.action = action;
		this.status = STATUS_OK;
	}

}
