package com.project.auth.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.jwk.JWKSet;
import com.project.auth.keys.RsaKeyManager;

/**
 * REST controller exposing JWT-related endpoints.
 * 
 * <p>
 * This controller provides:
 * </p>
 * <ul>
 *   <li>A JWKS (JSON Web Key Set) endpoint for public key discovery</li>
 * </ul>
 */
@RestController(value = "auth")
public class JwksController {
	
	@Autowired
	private RsaKeyManager keyManager;

	/**
	 * Exposes the JSON Web Key Set (JWKS).
	 *
	 * <p>
	 * Returns the active public RSA key in JWKS format, allowing
	 * external services to retrieve the public key required to
	 * verify JWT signatures.
	 * </p>
	 *
	 * <p>
	 * Endpoint: {@code GET /api/.well-known/jwks.json}
	 * </p>
	 *
	 * @return a map representing the JWKS JSON object
	 */
	@GetMapping(value = "/api/.well-known/jwks.json", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object> jwks() {
		JWKSet keySet = new JWKSet(keyManager.getActivePublicJwk());
		return keySet.toJSONObject();
	}
}
