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
