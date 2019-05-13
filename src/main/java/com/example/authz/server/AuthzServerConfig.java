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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.security.oauth2.provider.approval.Approval.ApprovalStatus;
import org.springframework.security.oauth2.provider.approval.InMemoryApprovalStore;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableAuthorizationServer
public class AuthzServerConfig extends AuthorizationServerConfigurerAdapter {
	
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private CustomAccessTokenConverter customAccessTokenConverter;
    
    @Autowired
    private UserService userService;
    
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    	
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
        
        endpoints.tokenStore(tokenStore())
        		 //.accessTokenConverter(accessTokenConverter())
                 .tokenEnhancer(tokenEnhancerChain)
                 .authenticationManager(authenticationManager)
                 .userDetailsService(userService);
    }
    
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }
    
    @Bean
    public TokenStore tokenStore() {
    	InMemoryJwtTokenStore store = new InMemoryJwtTokenStore(accessTokenConverter());      
    	
    	// Approval only supports a single scope, which is a bug.
    	// It should support multiple scopes. The workaround is to create multiple Approvals
    	InMemoryApprovalStore approvalStore = new InMemoryApprovalStore();
    	Approval approvalWrite = new Approval("mike", "SampleClientId", "write", 1000*60*60*24, ApprovalStatus.APPROVED);
    	Approval approvalRead = new Approval("mike", "SampleClientId", "read", 1000*60*60*24, ApprovalStatus.APPROVED);
    	List<Approval> approvalList = new ArrayList<>();
    	approvalList.add(approvalWrite);
    	approvalList.add(approvalRead);
    	approvalStore.addApprovals(approvalList); 	
    	store.setApprovalStore(approvalStore);
    	
    	return store;
    }
 
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
    	String signingKey = "symmetricSigningKey123";
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setAccessTokenConverter(customAccessTokenConverter);
        converter.setSigningKey(signingKey); // authz server
        converter.setVerifierKey(signingKey); // resource server
        
        return converter;
    }
 
    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        
    	DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        
        return defaultTokenServices;
    }
 
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()")
          .checkTokenAccess("isAuthenticated()");
    }
 
    /**
     * Client registration
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    	// Use database for dynamic client registration
    	//clients.jdbc(dataSource);
    	
    	// Use inMemory registration
        clients.inMemory()
          .withClient("SampleClientId")
          .secret(passwordEncoder.encode("secret"))
          .authorizedGrantTypes("refresh_token", "authorization_code", "password", "client_credentials") // , "password", "client_credentials"
          .scopes("read", "write")
          .autoApprove(false) 
          .redirectUris("http://localhost:8080/login", "http://localhost:8080/secret", "http://localhost:8080/access_tokens")
          .accessTokenValiditySeconds(60 * 60) // 1 hour
          .refreshTokenValiditySeconds(60 * 60 * 24); // 24 hours
          //-----
/*          .and()
          .withClient("sampleClientId")
          .authorizedGrantTypes("implicit")
          .scopes("read", "write", "foo", "bar")
          .autoApprove(false)
          .accessTokenValiditySeconds(3600)
          .redirectUris("http://localhost:8083/","http://localhost:8086/")
          .and()
          .withClient("fooClientIdPassword")
          .secret(passwordEncoder.encode("secret"))
          .authorizedGrantTypes("password", "authorization_code", "refresh_token", "client_credentials")
          .scopes("foo", "read", "write")
          .accessTokenValiditySeconds(3600)       // 1 hour
          .refreshTokenValiditySeconds(2592000)  // 30 days
          .redirectUris("http://www.example.com","http://localhost:8089/","http://localhost:8080/login/oauth2/code/custom","http://localhost:8080/ui-thymeleaf/login/oauth2/code/custom", "http://localhost:8080/authorize/oauth2/code/bael", "http://localhost:8080/login/oauth2/code/bael")
          .and()
          .withClient("barClientIdPassword")
          .secret(passwordEncoder.encode("secret"))
          .authorizedGrantTypes("password", "authorization_code", "refresh_token")
          .scopes("bar", "read", "write")
          .accessTokenValiditySeconds(3600)       // 1 hour
          .refreshTokenValiditySeconds(2592000)  // 30 days
          .and()
          .withClient("testImplicitClientId")
          .authorizedGrantTypes("implicit")
          .scopes("read", "write", "foo", "bar")
          .autoApprove(true)
          .redirectUris("http://www.example.com");*/
    }	

}
