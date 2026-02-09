package com.project.auth.repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.auth.entity.ClientEntity;
import com.project.auth.entity.RefreshTokenEntity;
import com.project.auth.entity.UserEntity;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {
	Optional<RefreshTokenEntity> findByTokenHash(String tokenHash);
	
	@Modifying
	@Query("""
			update RefreshTokenEntity rt
			  set rt.revokedAt = :now
			where rt.user = :user
			  and rt.client = :client
			  and rt.revokedAt is null
			""")
	int revokeAllActiveForUserClient(UserEntity user, ClientEntity client, Instant now);
}
