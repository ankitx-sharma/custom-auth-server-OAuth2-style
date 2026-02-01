package com.project.auth.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.jwk.JWKSet;
import com.project.auth.keys.RsaKeyManager;
import com.project.auth.tokens.JWTService;

@RestController(value = "auth")
public class JwksController {
	
	@Autowired
	private RsaKeyManager keyManager;
	
	@Autowired
	private JWTService jwtService;

	@GetMapping(value = "/api/.well-known/jwks.json", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object> jwks() {
		JWKSet keySet = new JWKSet(keyManager.getActivePublicJwk());
		return keySet.toJSONObject();
	}
	
	@GetMapping("/debug/token")
	public String test() {
		return jwtService.issueAccessToken("userId", List.of("read", "write"));
	}
}
