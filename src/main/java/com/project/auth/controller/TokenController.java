package com.project.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.auth.dto.TokenRequest;
import com.project.auth.dto.TokenResponse;
import com.project.auth.service.TokenService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class TokenController {
	
	@Autowired
	private TokenService tokenService;
	
	@PostMapping(value = "/api/oauth2/token",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public TokenResponse issueAccessToken(
			@RequestBody TokenRequest arguments,
			             HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
		String ip = request.getRemoteAddr();
		
		return tokenService.authenticate(arguments, userAgent, ip);
	}
}
