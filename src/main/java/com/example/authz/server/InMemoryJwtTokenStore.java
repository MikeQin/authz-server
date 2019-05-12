package com.example.authz.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InMemoryJwtTokenStore extends JwtTokenStore {
	
	Map<String, Collection<OAuth2AccessToken>> accessTokenStore = new HashMap<>();
	Map<String, Collection<OAuth2RefreshToken>> refreshTokenStore = new HashMap<>();

	public InMemoryJwtTokenStore(JwtAccessTokenConverter jwtTokenEnhancer) {
		super(jwtTokenEnhancer);		
	}

	@Override
	public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
		super.storeAccessToken(token, authentication);
		
		String clientId = authentication.getOAuth2Request().getClientId();
		
		if (accessTokenStore.get(clientId) == null) {
			Set<OAuth2AccessToken> tokenList = new HashSet<>();
			accessTokenStore.put(clientId, tokenList);
		}
		accessTokenStore.get(clientId).add(token);
	}

	@Override
	public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
		
		return accessTokenStore.get(clientId);		
	}

	@Override
	public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
		super.storeRefreshToken(refreshToken, authentication);
		
		log.info("[*] refreshToken stored: {}", refreshToken);
		
		String clientId = authentication.getOAuth2Request().getClientId();
		if (refreshTokenStore.get(clientId) == null) {
			Set<OAuth2RefreshToken> tokenList = new HashSet<>();
			refreshTokenStore.put(clientId, tokenList);
		}
		refreshTokenStore.get(clientId).add(refreshToken);
	}
	
	public Collection<OAuth2RefreshToken> findRefreshTokensByClientId(String clientId) {
		
		return refreshTokenStore.get(clientId);
	}

	@Override
	public void removeAccessToken(OAuth2AccessToken token) {
		super.removeAccessToken(token);
		
		OAuth2Authentication auth = readAuthentication(token);
		String clientId = auth.getOAuth2Request().getClientId();
		
		if (accessTokenStore.get(clientId).contains(token)) {
			accessTokenStore.get(clientId).remove(token);
			log.info("[x] removed client '{}' access_token: {}", clientId, token);
		}
		OAuth2RefreshToken refreshToken = token.getRefreshToken();
		if (refreshToken != null) {
			removeRefreshToken(refreshToken);
		}
	}

	@Override
	public void removeRefreshToken(OAuth2RefreshToken token) {
		super.removeRefreshToken(token);
		
		OAuth2Authentication auth = readAuthentication(token.getValue());
		String clientId = auth.getOAuth2Request().getClientId();		
		
		if (refreshTokenStore.get(clientId).contains(token)) {
			refreshTokenStore.get(clientId).remove(token);
			log.info("[x] removed client '{}' refresh_token: {}", clientId, token);
		}
	}
	
	
	
}
