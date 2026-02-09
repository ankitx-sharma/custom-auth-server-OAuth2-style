package com.project.auth.error;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OAuthErrorHandler {

	private static final Logger logger = LoggerFactory.getLogger(OAuthErrorException.class);
	
	@ExceptionHandler(OAuthErrorException.class)
	public ResponseEntity<Map<String, Object>> handle(OAuthErrorException ex) {
		logger.error("Exception occurred. Status: {}, Error Description: {}", ex.getStatus(), ex.getDescription());
		return ResponseEntity.status(ex.getStatus())
				.body(Map.of(
						"error", ex.getError(), 
						"error_description", ex.getDescription())
				);
	}
	
	@ExceptionHandler(RefreshTokenException.class)
	public ResponseEntity<Map<String, Object>> handleRefresh(RefreshTokenException ex) {
		return ResponseEntity.badRequest()
				.body(Map.of(
						"error", ex.getError(), 
						"error_description", ex.getDescription())
				);
	}
}
