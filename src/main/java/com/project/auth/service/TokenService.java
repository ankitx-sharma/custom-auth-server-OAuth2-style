package com.project.auth.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.project.auth.dto.TokenRequest;
import com.project.auth.dto.TokenResponse;
import com.project.auth.entity.ClientEntity;
import com.project.auth.entity.UserEntity;
import com.project.auth.error.OAuthErrorException;

@Service
public class TokenService {
	
	@Autowired
	private UserAuthService userService;
	
	@Autowired
	private ClientAuthService clientService;
	
	@Autowired
	private JWTService jwtService;
	
	@Autowired
	private RefreshTokenService refreshService;
	
	public TokenResponse authenticate(TokenRequest request, String userAgent, String ip) {
		if(!"password".equals(request.grantType())) {
			throw new OAuthErrorException(HttpStatus.BAD_REQUEST, "unsupported_grant_type", "Only password grant is supported in Phase 1");
		}
		
		ClientEntity client = clientService.authenticateClient(request.clientId(), request.clientSecret());
		
		return switch (request.grantType()) {
			case "password" -> handlePassword(client, request.username(), request.password(), request.scope(), userAgent, ip);
			case "refresh_token" -> handleRefresh(client, request.refresh_token(), userAgent, ip);
			default -> throw new OAuthErrorException(HttpStatus.BAD_REQUEST, 
					"unsupported_grant_type", "unsupported_grant_type");
			};
	}
	
	private TokenResponse handlePassword(ClientEntity client, String username, 
			String password, String scope, String userAgent, String ip) {
		clientService.assertGrantAllowed(client, "password");
		
		UserEntity user = userService.authenticateUser(username, password);
		Set<String> scopes = clientService.resolveAndValidateScopes(client, scope);
		
		String jwt = jwtService.issueAccessToken(user.getId().toString(), List.copyOf(scopes));
		String rawRefresh = refreshService.issue(user, client, userAgent, ip).rawToken();
		
		return new TokenResponse(jwt, rawRefresh, "Bearer", client.getAccessTokenTtlSec(), String.join(" ",scopes));
	}
	
	private TokenResponse handleRefresh(ClientEntity client, String presentRefreshToken, 
			String userAgent, String ip) {
		clientService.assertGrantAllowed(client, "refresh_token");
		
		if(presentRefreshToken == null || presentRefreshToken.isBlank()) {
			throw new OAuthErrorException(HttpStatus.BAD_REQUEST, "invalid_request", "Missing refresh_token");
		}
		
		var rotation = refreshService.rotate(presentRefreshToken, client, userAgent, ip);
		UserEntity user = rotation.user();
		
		Set<String> scopes = clientService.resolveAndValidateScopes(client, null);
		String jwt = jwtService.issueAccessToken(user.getId().toString(), List.copyOf(scopes));
		return new TokenResponse(jwt, rotation.newRawRefreshToken(), "Bearer", client.getAccessTokenTtlSec(), String.join(" ", scopes));
	}
}
