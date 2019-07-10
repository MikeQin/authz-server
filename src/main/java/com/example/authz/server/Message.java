/*******************************************************************************
 * Copyright (c) 2019 Michael Qin
 *
 * The freedom to run the program as you wish, for any purpose (freedom 0).
 *
 * The freedom to study how the program works, and change it so it does your computing 
 * as you wish (freedom 1). Access to the source code is a precondition for this.
 *
 * The freedom to redistribute copies so you can help your neighbor (freedom 2).
 *
 * The freedom to distribute copies of your modified versions to others (freedom 3). 
 * By doing this you can give the whole community a chance to benefit from your changes. 
 * Access to the source code is a precondition for this.
 *
 * Contributors:
 *     Michael Qin - initial API and implementation
 *******************************************************************************/
package com.example.authz.server;

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
