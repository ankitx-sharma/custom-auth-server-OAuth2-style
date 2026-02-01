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
@Table(name = "users")
public class UserEntity {
	@Id
	@GeneratedValue
	private UUID id;
	
	@Column(unique = true, nullable = false, length = 255)
	private String email;
	
	@Column(name = "password_hash", nullable = false, length = 255)
	private String passwordHash;
	
	@Column(nullable = false)
	private boolean enabled = true;
	
	@Column(name = "locked_until" , nullable = true)
	private Instant lockedUntil;
	
	@Column(name = "created_at", nullable = false)
	private Instant createdAt = Instant.now();
	
	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt = Instant.now();

	@PreUpdate
	public void preUpdate() { this.updatedAt = Instant.now(); }
	
	public UUID getId() { return id; }
	public void setId(UUID id) { this.id = id; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getPasswordHash() { return passwordHash; }
	public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

	public boolean isEnabled() { return enabled; }
	public void setEnabled(boolean enabled) { this.enabled = enabled; }

	public Instant getLockedUntil() { return lockedUntil; }
	public void setLockedUntil(Instant lockedUntil) { this.lockedUntil = lockedUntil; }

	public Instant getCreatedAt() { return createdAt; }
	public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

	public Instant getUpdatedAt() { return updatedAt; }
	public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
