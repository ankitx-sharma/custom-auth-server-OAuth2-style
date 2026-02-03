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
	
	public TokenResponse authenticate(TokenRequest request) {
		if(!"password".equals(request.grantType())) {
			throw new OAuthErrorException(HttpStatus.BAD_REQUEST, "unsupported_grant_type", "Only password grant is supported in Phase 1");
		}
		
		ClientEntity client = clientService.authenticateClient(request.clientId(), request.clientSecret());
		clientService.assertGrantAllowed(client, "password");
		
		UserEntity user = userService.authenticateUser(request.username(), request.password());
		Set<String> scopes = clientService.resolveAndValidateScopes(client, request.scope());
		
		String jwt = jwtService.issueAccessToken(user.getId().toString(), List.copyOf(scopes));
		
		return new TokenResponse(jwt, 
								"Bearer", 
								client.getAccessTokenTtlSec(), 
								String.join(" ", scopes)
								);
	}
}
