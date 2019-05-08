package com.example.authz.server;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//@FrameworkEndpoint
public class RevokeTokenEndpoint {

    @Resource(name = "tokenServices")
    ConsumerTokenServices tokenServices;

    @DeleteMapping(path = "/oauth/token")
    public @ResponseBody ResponseEntity<String> revokeToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        String status = "Revoke Token: ";
        if (authorization != null && authorization.contains("Bearer")) {
            String tokenId = authorization.substring("Bearer".length() + 1);
            status += tokenServices.revokeToken(tokenId);
        }
        
        return ResponseEntity.ok(status);
    }

}