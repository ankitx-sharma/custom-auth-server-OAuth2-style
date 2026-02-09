package com.project.auth.service;

import java.time.Instant;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.auth.entity.ClientEntity;
import com.project.auth.entity.RefreshTokenEntity;
import com.project.auth.entity.UserEntity;
import com.project.auth.error.RefreshTokenException;
import com.project.auth.keys.RefreshTokenCrypto;
import com.project.auth.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {
	
	private final Logger logger = LoggerFactory.getLogger(RefreshTokenService.class);
	
	@Autowired
	private RefreshTokenCrypto crypto;
	
	@Autowired
	private RefreshTokenRepository repository;
	
	/**
     * Issue a new refresh token for (user, client).
     * Returns the RAW token (only shown once to client).
     */
	@Transactional
	public IssuedRefreshToken issue(UserEntity user, ClientEntity client, String userAgent, String ip) {
		String raw = crypto.generateRawToken();
		String hash = crypto.hash(raw);
		
		Instant now = Instant.now();
		Instant exp = now.plusSeconds(client.getRefreshTokenttlSec());
		
		RefreshTokenEntity refreshToken = new RefreshTokenEntity(hash, user, client);
		refreshToken.setIssuedAt(now);
		refreshToken.setExpiresAt(exp);
		refreshToken.setUserAgent(userAgent);
		refreshToken.setIp(ip);
		
		repository.save(refreshToken);
		
		return new IssuedRefreshToken(raw, refreshToken);	
	}
	
	public RotationResult rotate(String presentedRawToken, ClientEntity client, 
			String userAgent, String ip) {
		String hash = crypto.hash(presentedRawToken);
		RefreshTokenEntity existing = repository.findByTokenHash(hash)
				.orElseThrow(() -> new RefreshTokenException("invalid_grant", "Invalid refresh token"));
		
		//Must belong to the same client
		if(!existing.getClient().getId().equals(client.getId())) {
			throw new RefreshTokenException("invalid_grant", "Invalid refresh token");
		}
		
		Instant now = Instant.now();
		
		if(existing.getExpiresAt().isBefore(now)) {
			throw new RefreshTokenException("invalid_grant", "Refresh token expired");
		}
		
		if(existing.getRevokedAt() != null) {
			existing.setReuseDetected(true);
			repository.save(existing);
			
			// revoke all active for that user+client 
			UserEntity user = existing.getUser();
			int count = repository.revokeAllActiveForUserClient(user, client, now);
			
			logger.debug("REFRESH_REUSE_DETECTED", Map.of("userId", user!=null ? user.getId().toString(): "null", 
													"clientId", client.getClientId(), "revokedActiveCount", count));
			
			throw new RefreshTokenException("invalid_grant", "Refresh token reuse detected");
		}
		
		IssuedRefreshToken newToken = issue(existing.getUser(), client, userAgent, ip);
		
		existing.setRevokedAt(now);
		existing.setReplacedByToken(newToken.dbToken());
		repository.save(existing);
		
		logger.debug("REFRESH_ROTATED", Map.of(
				"userId", existing.getUser()!=null ? existing.getUser().getId().toString() : "null",
				"clientId", client.getClientId()));
		
		return new RotationResult(existing.getUser(), newToken.rawToken());
	}
	
	public record IssuedRefreshToken(String rawToken, RefreshTokenEntity dbToken) {}
	public record RotationResult(UserEntity user,  String newRawRefreshToken) {}
	
}
