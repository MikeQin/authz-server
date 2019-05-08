package com.example.resource.server;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.authz.annotation.IsAdmin;
import com.example.authz.annotation.IsReader;
import com.example.authz.annotation.IsWriter;
import com.example.authz.server.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * access = "#oauth2.hasScope('read') or (#oauth2.hasScope('other') and hasRole('ROLE_USER'))"
 * 
 * @author qin682
 *
 */
@RestController
@Slf4j
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {
	
	@Autowired
	UserService userService;

	/**
	 * User info endpoint
	 * 
	 * @param principal
	 * @return
	 */
	@IsWriter
	@GetMapping(path = "/user/me")
	public @ResponseBody ResponseEntity<Authentication> user(Principal principal) {
		
		Authentication auth = null;
		//User user = null;
		
		if (principal instanceof Authentication) {
			
			auth = (Authentication) principal;
			//user = new User(oauth2Auth);		
			
			log.info(auth.toString());
		}

		return ResponseEntity.ok(auth);
	}

	/**
	 * User Extra
	 * @param auth
	 * @return
	 */
	@IsReader
	@GetMapping(path = "/user/extra")
	public @ResponseBody ResponseEntity<Map<String, Object>> getExtraInfo(Authentication auth) {
		
		OAuth2AuthenticationDetails oauthDetails = (OAuth2AuthenticationDetails) auth.getDetails();
		
		@SuppressWarnings("unchecked")
		Map<String, Object> details = (Map<String, Object>) oauthDetails.getDecodedDetails();
		log.info("User organization is " + details.get("organization"));
		
		return ResponseEntity.ok(details);
	}
	
	class User {
		
		private Authentication auth;
		
		public User(Authentication auth) {
			this.auth = auth;
		}
		
		public String getName() {
			return auth.getName().toUpperCase();
		}
	}
	
	@IsAdmin
	@GetMapping(path = "/user/details")
	public @ResponseBody ResponseEntity<UserDetails> getUserDetails(Authentication auth) {
		
		// Note, that userService dependency should be removed here. 
		// Instead, getUserDetails from Authz Server endpoints.
		// Here we use it for demo purpose only
		UserDetails ud = userService.loadUserByUsername(auth.getName());
		
		return ResponseEntity.ok(ud);
	}
}
