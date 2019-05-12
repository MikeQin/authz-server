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
	 * Path: /hello
	 * 
	 * @param auth
	 * @return
	 */
	@IsWriter
	@GetMapping(path = "/secret", produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody ResponseEntity<String> echo(Authentication auth) {
		
		log.info("[*] enter /secret");

		String message = "Congratulation, " + auth.getName().toUpperCase() 
				+ "! You entered the secret resource area which is protected by OAuth2."
				+ " Only user who has 'ADMIN' role and scope 'write' can access here.";

		return ResponseEntity.ok(message);
	}
}