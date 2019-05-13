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


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
public class GlobalMethodSecurityTests {
	
	@Autowired
	MockService mockService; 

	@Test
	@WithMockUser(username = "mike", roles = { "ADMIN" })
	public void testIsAdminForAdmin() {
		
		assertThat(mockService.echo("John")).isEqualTo("John");
		
	}	
	
	@Test(expected = AccessDeniedException.class)
	@WithMockUser(username = "mike", roles = { "USER" })
	public void testIsAdminForUser() {

		assertThat(mockService.echo("John")).isEqualTo("John");
		
	}
	
	@Test(expected = AccessDeniedException.class)
	@WithAnonymousUser
	public void testIsAdminForAnonymous() {
		
		assertThat(mockService.echo("John")).isEqualTo("John");
		
	}
	
	@Test
	@WithMockUser(username = "john", roles = { "USER" })
	public void testPreFilterOnJoinNames() {
		String[] names = {"mike", "john", "joe"};
		List<String> namesList = Stream.of(names).collect(Collectors.toList());
		String usernames = mockService.joinNamesWithoutPrincipal(namesList);
		
		assertThat(usernames).doesNotContain("john");
	}
}
