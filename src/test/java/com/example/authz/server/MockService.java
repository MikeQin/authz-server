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
