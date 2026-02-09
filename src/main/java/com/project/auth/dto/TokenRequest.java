package com.project.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenRequest(
	@JsonProperty("grant_type") String grantType,
	@JsonProperty("username") String username,
	@JsonProperty("password") String password,
	@JsonProperty("refresh_token") String refresh_token,
	@JsonProperty("client_id") String clientId,
	@JsonProperty("client_secret") String clientSecret,
	@JsonProperty("scope") String scope
) {}