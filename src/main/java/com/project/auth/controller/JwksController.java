package com.project.auth.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.jwk.JWKSet;
import com.project.auth.keys.RsaKeyManager;

@RestController(value = "auth")
public class JwksController {
	
	@Autowired
	private RsaKeyManager keyManager;

	@GetMapping(value = "/api/.well-known/jwks.json", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object> jwks() {
		JWKSet keySet = new JWKSet(keyManager.getActivePublicJwk());
		return keySet.toJSONObject();
	}
}
