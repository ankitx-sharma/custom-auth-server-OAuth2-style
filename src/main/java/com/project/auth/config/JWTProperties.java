package com.project.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jwt")
public class JWTProperties {

	// Example: https://auth.local
	private String issuer;
	
	// Example: "auth-api"
	private String audience = "auth-api";
	
	// Access token TTL in seconds
	private long accessTokenTtlSeconds = 900;

	public String getIssuer() { return issuer; }
	public void setIssuer(String issuer) { this.issuer = issuer; }

	public String getAudience() { return audience; }
	public void setAudience(String audience) { this.audience = audience; }

	public long getAccessTokenTtlSeconds() { return accessTokenTtlSeconds; }
	public void setAccessTokenTtlSeconds(long accessTokenTtlSeconds) { this.accessTokenTtlSeconds = accessTokenTtlSeconds; }
}
