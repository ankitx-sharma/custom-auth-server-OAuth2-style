package com.project.auth.keys;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.nimbusds.jose.jwk.RSAKey;

import jakarta.annotation.PostConstruct;

/**
 * Manages RSA keys used for signing and verifying JWTs.
 *
 * <p>
 * This component is responsible for:
 * </p>
 * <ul>
 *   <li>Generating an RSA key pair at application startup</li>
 *   <li>Exposing the active private key for token signing</li>
 *   <li>Exposing the corresponding public key as a JWK for JWKS publishing</li>
 * </ul>
 *
 */
@Component
public class RsaKeyManager {
	
	private RSAKey activeKey;
	
	/**
	 * Initializes the RSA key manager after bean creation.
	 *
	 * <p>
	 * Generates a new RSA key pair and stores it as the active key.
	 * </p>
	 */
	@PostConstruct
	public void init() {
		this.activeKey = generateRSAJwk();
	}
	
	/**
	 * Returns the active RSA key, including the private key.
	 *
	 * @return the active {@link RSAKey}
	 */
	public RSAKey getActiveKey() { return this.activeKey; }
	
	/**
	 * Returns the public portion of the active RSA key as a JWK.
	 *
	 * <p>
	 * Used for exposing the public key via a JWKS endpoint so that
	 * resource servers can verify JWT signatures.
	 * </p>
	 *
	 * @return the public {@link RSAKey} JWK
	 */
	public RSAKey getActivePublicJwk() { return this.activeKey.toPublicJWK(); }
	
	/**
	 * Generates a new RSA key pair and wraps it as a JWK.
	 *
	 * <p>
	 * The key pair is generated using a 2048-bit RSA algorithm
	 * and assigned a random key ID ({@code kid}).
	 * </p>
	 *
	 * @return a newly generated {@link RSAKey}
	 * @throws IllegalStateException if key generation fails
	 */
	private RSAKey generateRSAJwk() {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(2048);
			KeyPair pair = generator.generateKeyPair();
			
			RSAPublicKey publicKey = (RSAPublicKey) pair.getPublic();
			RSAPrivateKey privateKey = (RSAPrivateKey) pair.getPrivate();
			
			return new RSAKey.Builder(publicKey)
					.privateKey(privateKey)
					.keyID(UUID.randomUUID().toString())
					.build();
		} catch (Exception e) {
			throw new IllegalStateException("Failed to generate RSA Key pair: ",e);
		}
	}
}
