package com.example.authz.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

/**
 * - The prePostEnabled property enables Spring Security @preAuthorize and @postAuthorize annotations
 * - The securedEnabled property determines if the @Secured annotation should be enabled.
 * - The jsr250Enabled property allows us to use the @RoleAllowed annotation
 * 
 * @author qin682
 *
 */
@Configuration
@EnableGlobalMethodSecurity(
		prePostEnabled = true, securedEnabled = false, jsr250Enabled = false)
public class GlobalMethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return new OAuth2MethodSecurityExpressionHandler();
    }

}