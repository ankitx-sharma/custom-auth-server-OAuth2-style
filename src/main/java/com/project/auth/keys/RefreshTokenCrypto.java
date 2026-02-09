package com.project.auth.keys;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.auth.config.RefreshTokenProperties;

@Component
public class RefreshTokenCrypto {
	
	private static final SecureRandom RNG = new SecureRandom();
	private static final Base64.Encoder B64 = Base64.getUrlEncoder().withoutPadding();
	
	@Autowired
	private RefreshTokenProperties properties;
	
	public String generateRawToken() {
		byte[] bytes = new byte[32]; //256 bit
		RNG.nextBytes(bytes);
		return "r1_"+B64.encodeToString(bytes);
	}
	
	/**
     * Hash refresh token using HMAC-SHA256 with pepper as key.
     * Output: URL-safe base64 string.
     */
	public String hash(String rawToken) {
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec key = new SecretKeySpec(properties.getPepper().getBytes(), "HmacSHA256");
			mac.init(key);
			byte[] out = mac.doFinal(rawToken.getBytes());
			return B64.encodeToString(out);
		} catch (Exception ex) {
			throw new IllegalArgumentException("Failed to hash refresh token", ex);
		}
	}
}
