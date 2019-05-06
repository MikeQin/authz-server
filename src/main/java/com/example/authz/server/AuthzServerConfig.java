package com.example.authz.server;

import java.util.Arrays;

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
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthzServerConfig extends AuthorizationServerConfigurerAdapter {
	
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private CustomAccessTokenConverter customAccessTokenConverter;
    
    /**
     * ======================== New =========================================
     */
    
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    	
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
        
        endpoints.tokenStore(tokenStore())
                 .tokenEnhancer(tokenEnhancerChain)
                 .authenticationManager(authenticationManager);
    }
    
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }
    
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }
 
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
    	
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setAccessTokenConverter(customAccessTokenConverter);
        converter.setSigningKey("symmetricSigningKey123");
        
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
    
    /**
     * ======================== Old =========================================
     */
 
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
        clients.inMemory()
          .withClient("SampleClientId")
          .secret(passwordEncoder.encode("secret"))
          .authorizedGrantTypes("password", "authorization_code", "refresh_token") // 
          .scopes("user_info")
          .autoApprove(true) 
          .redirectUris(
        		  "http://localhost:8080/login",
        		  "http://www.example.com/")
          .accessTokenValiditySeconds(3600) // 1 hour
          //----------------------------------------------------------
          .and()
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
          .redirectUris("http://www.example.com");
    }	

}
