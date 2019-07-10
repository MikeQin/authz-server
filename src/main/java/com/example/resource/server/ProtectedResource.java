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
package com.example.resource.server;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.authz.annotation.IsWriter;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ProtectedResource {
	/**
	 * Path: /secret
	 * 
	 * @param auth
	 * @return
	 */
	@IsWriter
	@GetMapping(path = "/secret", produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody ResponseEntity<String> secret(Authentication auth) {
		
		log.info("[*] enter /secret");

		String message = "Congratulation, " + auth.getName().toUpperCase() 
				+ "! You entered the secret resource area which is protected by OAuth2."
				+ " Only user who has 'ADMIN' role and scope 'write' can access here.";

		return ResponseEntity.ok(message);
	}
	
	/**
	 * Path: /hello
	 * 
	 * @param auth
	 * @return
	 */
	@IsWriter
	@GetMapping(path = "/hello", produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody ResponseEntity<String> hello(Authentication auth) {
		
		log.info("[*] enter /hello");

		String message = "Hello, " + auth.getName().toUpperCase() + "! You're in proected area";

		return ResponseEntity.ok(message);
	}
}
