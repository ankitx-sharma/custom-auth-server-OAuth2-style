package com.project.auth.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "clients")
public class ClientEntity {
	
	@Id
	@GeneratedValue
	private UUID id;
	
	@Column(name = "client_id", nullable = false, unique = true, length = 128)
	private String clientId;
	
	@Column(name = "client_secret_hash", nullable = false, length = 255)
	private String secretHash;
	
	@Column(nullable = false, length = 255)
	private String name;
	
	@Column(name = "allowed_grants", nullable = false)
	private String allowedGrants;
	
	@Column(name = "allowed_scopes", nullable = false)
	private String allowedScopes;
	
	@Column(name = "access_token_ttl_seconds", nullable = false)
	private int accessTokenTtlSec = 900;
	
	@Column(name = "refresh_token_ttl_seconds", nullable = false)
	private int refreshTokenttlSec = 2592000;
	
	@Column(name = "created_at", nullable = false)
	private Instant createdAt = Instant.now();
	
	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt = Instant.now();
	
	@PreUpdate
	public void preUpdate() { this.updatedAt = Instant.now(); }
	
	public UUID getId() { return id; }
	public void setId(UUID id) { this.id = id; }

	public String getClientId() { return clientId; }
	public void setClientId(String clientId) { this.clientId = clientId; }

	public String getSecretHash() { return secretHash; }
	public void setSecretHash(String secretHash) { this.secretHash = secretHash; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getAllowedGrants() { return allowedGrants; }
	public void setAllowedGrants(String allowedGrants) { this.allowedGrants = allowedGrants; }

	public String getAllowedScopes() { return allowedScopes; }
	public void setAllowedScopes(String allowedScopes) { this.allowedScopes = allowedScopes; }

	public int getAccessTokenTtlSec() { return accessTokenTtlSec; }
	public void setAccessTokenTtlSec(int accessTokenTtlSec) { this.accessTokenTtlSec = accessTokenTtlSec; }

	public int getRefreshTokenttlSec() { return refreshTokenttlSec; }
	public void setRefreshTokenttlSec(int refreshTokenttlSec) { this.refreshTokenttlSec = refreshTokenttlSec; }

	public Instant getCreatedAt() { return createdAt; }
	public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

	public Instant getUpdatedAt() { return updatedAt; }
	public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
