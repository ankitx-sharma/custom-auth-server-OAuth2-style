package com.project.auth.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.auth.entity.UserEntity;
import com.project.auth.error.OAuthErrorException;
import com.project.auth.repository.UserRepository;
import com.project.auth.util.AppUtil;

@Service
public class UserAuthService {

	private static final String INVALID_GRANT = "invalid_grant";

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	public UserEntity authenticateUser(String username, String password) {
		if(AppUtil.isBlank(username) || AppUtil.isBlank(password)) {
			throw new OAuthErrorException(HttpStatus.BAD_REQUEST, "invalid_request", "Missing username or password");
		}
		
		UserEntity user = userRepo.findByEmail(username).orElseThrow(() -> {
			throw new OAuthErrorException(HttpStatus.BAD_REQUEST, INVALID_GRANT, "Invalid credentials");
		});
		
		if(!user.isEnabled()) {
			throw new OAuthErrorException(HttpStatus.BAD_REQUEST, INVALID_GRANT, "User disabled");
		}
		
		if(user.getLockedUntil() != null && user.getLockedUntil().isAfter(Instant.now())) {
			throw new OAuthErrorException(HttpStatus.BAD_REQUEST, INVALID_GRANT, "User temporarily locked");
		}
		
		if(encoder.matches(password, user.getPasswordHash())) {
			throw new OAuthErrorException(HttpStatus.BAD_REQUEST, INVALID_GRANT, "Invalid credentials");
		}
		
		return user;
	}
}
