package com.project.auth.keys;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.nimbusds.jose.jwk.RSAKey;

import jakarta.annotation.PostConstruct;

@Component
public class RsaKeyManager {
	
	private RSAKey activeKey;
	
	@PostConstruct
	public void init() {
		this.activeKey = generateRSAJwk();
	}
	
	public RSAKey getActiveKey() { return this.activeKey; }
	public RSAKey getActivePublicJwk() { return this.activeKey.toPublicJWK(); }
	
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
