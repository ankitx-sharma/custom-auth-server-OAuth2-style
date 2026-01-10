package com.project.auth.config;

import java.util.Base64;

import javax.crypto.SecretKey;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.project.auth.util.JwtProperties;

import io.jsonwebtoken.security.Keys;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtKeyConfig {
	
	@Bean
	public SecretKey jwtSigningKey(JwtProperties props) {
		String base64 = props.getSecretBase64();
		
		if(base64 == null || base64.isBlank()) {
			throw new IllegalStateException(
				"Missing JWT Secret. Set env JWT_SECRET_BASE64 or jwt.secret-base64 in config."
			);
		}
		
		byte[] keyBytes;
		try {
			keyBytes = Base64.getDecoder().decode(base64);
		} catch(IllegalArgumentException ex) {
			throw new IllegalStateException(
				"JWT secret is not valid Base64. ", ex);
		}
		
		int minBytesForHs512 = 64; // 512 bites
		if(keyBytes.length < minBytesForHs512) {
			throw new IllegalStateException(
				"JWT secret is too short for HS512. Provide at least 64 bytes (512 bites) AFTER base64 decoding."
			);
		}
		
		// JJWT will also validate algorithm constraints when using Keys.hmacShaKeyFor
		return Keys.hmacShaKeyFor(keyBytes);
		
	}

}
