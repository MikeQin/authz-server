package com.example.resource.server;

import static com.example.resource.server.Message.ACCESS_TOKEN;
import static com.example.resource.server.Message.ACTION_REVOKE;
import static com.example.resource.server.Message.REFRESH_TOKEN;
import static com.example.resource.server.Message.STATUS_500;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.authz.annotation.IsWriter;
import com.example.authz.server.InMemoryJwtTokenStore;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class AdminController {

	@Autowired
	DefaultTokenServices tokenServices;

	@Autowired
	TokenStore tokenStore;

	/**
	 * Path: /tokens/{token:.*}
	 * 
	 * @param token
	 * @param request
	 * @return
	 */
	@IsWriter
	@DeleteMapping(path = "/tokens/{token:.*}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ResponseEntity<Message> revokeAccessToken(@PathVariable String token,
			HttpServletRequest request) {

		String authorization = request.getHeader("Authorization");
		if (authorization != null && authorization.contains("Bearer")) {
			String extractedTokenId = authorization.substring("Bearer".length() + 1);
			// tokenServices.revokeToken(tokenId);
			log.info("[*] extractedTokenId {}", extractedTokenId);
		}

		Message message = new Message(ACCESS_TOKEN, token, ACTION_REVOKE);
		try {
			tokenServices.revokeToken(token);
		} catch (Exception e) {
			message.setStatus(STATUS_500);
			message.setError(e.getMessage());
			
			return ResponseEntity.status(500).body(message);
		}

		log.info("[*] {}", message);

		return ResponseEntity.ok(message);
	}

	/**
	 * Path: /tokens/refresh/{token:.*}
	 * 
	 * @param token
	 * @param request
	 * @return
	 */
	@IsWriter
	@DeleteMapping(path = "/tokens/refresh/{token:.*}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ResponseEntity<Message> revokeRefreshToken(@PathVariable String token,
			OAuth2Authentication auth) {

		log.info("[x] auth scope: {}", auth.getOAuth2Request().getScope());

		OAuth2RefreshToken refreshToken = tokenStore.readRefreshToken(token);

		Message message = new Message(REFRESH_TOKEN, token, ACTION_REVOKE);

		try {
			tokenStore.removeRefreshToken(refreshToken);
		} catch (Exception e) {
			message.setStatus(STATUS_500);
			message.setError(e.getMessage());
			
			return ResponseEntity.status(500).body(message);
		}

		log.info("[*] {}", message);

		return ResponseEntity.ok(message);
	}

	/**
	 * Path: /tokens
	 * 
	 * @return
	 */
	@GetMapping(path = "/tokens", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ResponseEntity<List<String>> getTokens() {
		List<String> tokenValues = new ArrayList<>();
		Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientId("SampleClientId");

		log.info("tokens: " + tokens);

		if (tokens != null) {
			for (OAuth2AccessToken token : tokens) {
				tokenValues.add(token.getValue());
			}
		}

		return ResponseEntity.ok(tokenValues);
	}

	/**
	 * Path: /tokens/refresh
	 * 
	 * @return
	 */
	@GetMapping(path = "/tokens/refresh", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ResponseEntity<List<String>> getRefreshTokens() {
		List<String> tokenValues = new ArrayList<>();
		Collection<OAuth2RefreshToken> tokens = ((InMemoryJwtTokenStore) tokenStore)
				.findRefreshTokensByClientId("SampleClientId");

		log.info("refresh_tokens: " + tokens);

		if (tokens != null) {
			for (OAuth2RefreshToken token : tokens) {
				tokenValues.add(token.getValue());
			}
		}

		return ResponseEntity.ok(tokenValues);
	}

	/**
	 * Path: /hello
	 * 
	 * @param auth
	 * @return
	 */
	@IsWriter
	@GetMapping(path = "/hello", produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody ResponseEntity<String> echo(Authentication auth) {
		
		log.info("[*] enter /hello");

		String message = "Hello, " + auth.getName();

		return ResponseEntity.ok(message);
	}
}
