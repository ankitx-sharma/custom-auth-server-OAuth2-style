package com.project.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for JWT (JSON Web Token) Handling
 */
@ConfigurationProperties(prefix = "app.jwt")
public class JWTProperties {

	/**
	 * The expected issuer ({@code iss}) claim of the JWT
	 * 
	 * <p>
	 * Typically the URL of the authorization server that issued the token.
	 * </p>
	 */
	private String issuer;
	
	/**
	 * The expected audience ({@code aud}) claim of the JWT.
	 * 
	 * <p>
	 * Represents the intended recipient of the token, usually the resource server or API name.
	 * </p>
	 */
	private String audience = "auth-api";
	
	/**
	 * Time-to-live (TTL) for access tokens, in seconds.
	 */
	private long accessTokenTtlSeconds = 900;

	public String getIssuer() { return issuer; }
	public void setIssuer(String issuer) { this.issuer = issuer; }

	public String getAudience() { return audience; }
	public void setAudience(String audience) { this.audience = audience; }

	public long getAccessTokenTtlSeconds() { return accessTokenTtlSeconds; }
	public void setAccessTokenTtlSeconds(long accessTokenTtlSeconds) { this.accessTokenTtlSeconds = accessTokenTtlSeconds; }
}
