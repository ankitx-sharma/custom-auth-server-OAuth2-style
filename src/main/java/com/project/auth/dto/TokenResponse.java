package com.project.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TokenResponse (
	@JsonProperty("access_token") String accessToken,
	@JsonProperty("refresh_token") String refreshToken,
	@JsonProperty("token_type") String tokenType,
	@JsonProperty("expires_in") long expiresIn,
	@JsonProperty("scope") String scope
) {}
