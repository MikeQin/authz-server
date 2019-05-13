package com.example.authz.server;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@FrameworkEndpoint
@Slf4j
public class RevokeTokenEndpoint {

	@Resource(name = "tokenServices")
	ConsumerTokenServices tokenServices;

	@DeleteMapping(path = "/oauth/token")
	@ResponseBody
	public void revokeToken(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");
		if (authorization != null && authorization.contains("Bearer")) {
			String tokenId = authorization.substring("Bearer".length() + 1);
			tokenServices.revokeToken(tokenId);

			log.info("[*] access_token was revoked successfully from @FrameworkEndpoint");
		}
	}
}