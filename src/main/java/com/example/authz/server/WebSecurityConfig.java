package com.example.authz.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@Order(1)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
		// Permit All
		.requestMatchers()
		.antMatchers("/login", "/oauth/authorize")
		.antMatchers("/oauth/token/revokeById/**")
		.antMatchers("/tokens/**")
		// Authentication for All except for the above
		.and().authorizeRequests().anyRequest().authenticated()
		// Form Login is allowed
		.and().formLogin().permitAll()
		// Disable Cross Site Request Forgery Check
		.and().csrf().disable();
	}
	
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	/**
	 * User database for authentication
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
		.withUser("john").password(passwordEncoder().encode("123")).roles("USER")
		.and()
		.withUser("mike").password(passwordEncoder().encode("123")).roles("USER", "ADMIN");
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}