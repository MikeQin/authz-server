package com.example.authz.server;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Service;

import com.example.authz.annotation.IsAdmin;

@Service
public class MockService {

	@IsAdmin
	public String echo(String name) {
		
		return name;
	}
	
	@PreFilter("filterObject != authentication.principal.username")
	public String joinNamesWithoutPrincipal(Collection<String> usernames) {
		return usernames.stream().collect(Collectors.joining(";"));
	}
}
