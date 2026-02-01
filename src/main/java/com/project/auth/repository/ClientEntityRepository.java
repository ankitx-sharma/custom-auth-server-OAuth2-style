package com.project.auth.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.auth.entity.ClientEntity;

@Repository
public interface ClientEntityRepository extends JpaRepository<ClientEntity, UUID>{
	public Optional<ClientEntity> findByClientId(String clientId);
	public boolean existsByClientId(String clientId);
}
