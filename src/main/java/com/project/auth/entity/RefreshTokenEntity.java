package com.project.auth.entity;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "refresh_tokens", indexes = {
	@Index(name = "idx_refresh_tokens_user_id", columnList = "user"),
	@Index(name = "idx_refresh_tokens_client_id", columnList = "client"),
	@Index(name = "idex_refresh_tokens_expires_at", columnList = "expiresAt")
})
public class RefreshTokenEntity {
	
	@Id
	@GeneratedValue
	private UUID id;
	
	@Column(name = "token_hash", nullable = false, unique = true, length = 255)
	private String tokenHash;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserEntity user;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "client_id", nullable = false)
	private ClientEntity client;
	
	@Column(name = "issued_at", nullable = false)
	private Instant issuedAt = Instant.now();
	
	@Column(name = "expires_at", nullable = false)
	private Timestamp expiresAt;
	
	@Column(name = "revoked_at")
	private Instant revokedAt;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "replaced_by_token_id")
	private RefreshTokenEntity replacedBYToken;
	
	@Column(name = "reuse_detected", nullable = false)
	private boolean reuseDetected = false;
	
	@Column(name = "user_agent")
	private String userAgent;
	
	@Column(name = "ip")
	private String ip;

	public RefreshTokenEntity() {
		super();
	}

	public RefreshTokenEntity(String tokenHash, UserEntity user, ClientEntity client) {
		super();
		this.tokenHash = tokenHash;
		this.user = user;
		this.client = client;
	}

	public String getTokenHash() {
		return tokenHash;
	}

	public void setTokenHash(String tokenHash) {
		this.tokenHash = tokenHash;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public ClientEntity getClient() {
		return client;
	}

	public void setClient(ClientEntity client) {
		this.client = client;
	}

	public Instant getIssuedAt() {
		return issuedAt;
	}

	public void setIssuedAt(Instant issuedAt) {
		this.issuedAt = issuedAt;
	}

	public Timestamp getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Timestamp expiresAt) {
		this.expiresAt = expiresAt;
	}

	public Instant getRevokedAt() {
		return revokedAt;
	}

	public void setRevokedAt(Instant revokedAt) {
		this.revokedAt = revokedAt;
	}

	public RefreshTokenEntity getReplacedByToken() {
		return replacedBYToken;
	}

	public void setReplacedByToken(RefreshTokenEntity replacedBYToken) {
		this.replacedBYToken = replacedBYToken;
	}

	public boolean isReuseDetected() {
		return reuseDetected;
	}

	public void setReuseDetected(boolean reuseDetected) {
		this.reuseDetected = reuseDetected;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public UUID getId() {
		return id;
	}
}
