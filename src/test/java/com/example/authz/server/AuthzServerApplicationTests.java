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

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import io.restassured.response.Response;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AuthzServerApplicationTests {
	
	@LocalServerPort
    private int port;

	@Autowired
	private JwtTokenStore tokenStore;

	@Test
	public void contextLoads() {
	}

	@Test
	public void whenTokenContainsExtra_thenSuccess() {
		String tokenValue = obtainAccessToken("SampleClientId", "secret", "john", "123");
		OAuth2Authentication auth = tokenStore.readAuthentication(tokenValue);
		@SuppressWarnings("unchecked")
		Map<String, Object> details = (Map<String, Object>) auth.getDetails();

		assertTrue(details.containsKey("organization"));
	}

	private String obtainAccessToken(String clientId, String clientSecret, String username, String password) {

		String tokenEndpoint = String.format("http://localhost:%d/auth/oauth/token", port);
		
		System.out.println(tokenEndpoint);
		
		Map<String, String> params = new HashMap<>();
		params.put("grant_type", "password");
		params.put("username", username);
		params.put("password", password);
		Response response = RestAssured.given().auth().preemptive().basic(clientId, clientSecret)
				.and().with().params(params).when().post(tokenEndpoint);
		
		return response.jsonPath().getString("access_token");
	}

}
