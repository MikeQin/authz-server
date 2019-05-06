package com.example.authz.server;

import java.security.Principal;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	/**
	 * User Info Endpoint
	 * @param principal
	 * @return
	 */
	@GetMapping(path = "/user/me", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public User user(Principal principal) {

		System.out.println("[*] " + principal);

		return new User(principal);
	}

	/**
	 * User Extra
	 * @param auth
	 * @return
	 */
	@PreAuthorize("#oauth2.hasScope('read')")
	@GetMapping(path = "/user/extra")
	@ResponseBody
	public Map<String, Object> getExtraInfo(Authentication auth) {
		OAuth2AuthenticationDetails oauthDetails = (OAuth2AuthenticationDetails) auth.getDetails();
		@SuppressWarnings("unchecked")
		Map<String, Object> details = (Map<String, Object>) oauthDetails.getDecodedDetails();
		System.out.println("User organization is " + details.get("organization"));
		return details;
	}
	
	class User {
		
		private Principal principal;
		
		public User(Principal principal) {
			this.principal = principal;
		}
		
		public String getName() {
			return principal.getName().toUpperCase();
		}
	}
}
