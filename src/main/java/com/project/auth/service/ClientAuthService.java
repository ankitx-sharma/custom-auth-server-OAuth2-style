package com.project.auth.service;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.auth.entity.ClientEntity;
import com.project.auth.error.OAuthErrorException;
import com.project.auth.repository.ClientEntityRepository;
import com.project.auth.util.AppUtil;

@Service
public class ClientAuthService {

	private static final String INVALID_CLIENT = "invalid_client";

	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private ClientEntityRepository clientRepo;
	
	public ClientEntity authenticateClient(String clientId, String clientSecret) {
		if(AppUtil.isBlank(clientId) || AppUtil.isBlank(clientSecret)) {
			throw new OAuthErrorException(HttpStatus.UNAUTHORIZED, INVALID_CLIENT, "Missing client credentials");
		}
		
		ClientEntity client = clientRepo.findByClientId(clientId).orElseThrow( () ->
				new OAuthErrorException(HttpStatus.UNAUTHORIZED, INVALID_CLIENT, "Invalid client"));
		
		if(!encoder.matches(clientSecret, client.getSecretHash())) {
			throw new OAuthErrorException(HttpStatus.UNAUTHORIZED, INVALID_CLIENT, "Invalid client");
		}
		
		return client;
	}
	
	public void assertGrantAllowed(ClientEntity client, String grantType) {
		Set<String> allowed = splitSpaceSeparated(client.getAllowedGrants());
		if(!allowed.contains(grantType)) {
			throw new OAuthErrorException(HttpStatus.BAD_REQUEST, "unauthorized_client", "Grant not allowed for this client");
		}
	}
	
	public Set<String> resolveAndValidateScopes(ClientEntity client, String requestScopes) {
		Set<String> allowed = splitSpaceSeparated(client.getAllowedGrants());
		
		if(AppUtil.isBlank(requestScopes)) {
			return allowed;
		}
		
		Set<String> requested = splitSpaceSeparated(requestScopes);
		
		if(!allowed.containsAll(requested)) {
			throw new OAuthErrorException(HttpStatus.BAD_REQUEST, "invalid_scope", "Requested scope is not allowed");
		}
		
		return requested;
	}
	
	private Set<String> splitSpaceSeparated(String text) {
		return text == null ? Set.of() : Arrays.stream(text.trim().split("\\s+"))
													.collect(Collectors.toSet());
	}
}
