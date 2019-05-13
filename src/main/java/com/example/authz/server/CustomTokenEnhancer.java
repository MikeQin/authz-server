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

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

public class CustomTokenEnhancer implements TokenEnhancer {
	
    @Override
    public OAuth2AccessToken enhance(
      OAuth2AccessToken accessToken, 
      OAuth2Authentication authentication) {
        Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put(
          "organization", authentication.getName() + "-" + randomAlphabetic(4));
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(
          additionalInfo);
        return accessToken;
    }
}
