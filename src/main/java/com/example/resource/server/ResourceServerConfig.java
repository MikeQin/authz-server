package com.example.resource.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Autowired
	DefaultTokenServices tokenServices;

	@Override
	public void configure(ResourceServerSecurityConfigurer config) {
		config.tokenServices(tokenServices);
	}

//	@Override
//	public void configure(final HttpSecurity http) throws Exception {
//
//		http
//			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//			.and()
//			.authorizeRequests().anyRequest().permitAll();
//
//	}

}