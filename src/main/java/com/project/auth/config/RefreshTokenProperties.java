package com.project.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.refresh")
public class RefreshTokenProperties {

	/**
     * Secret pepper used in hashing refresh tokens. For real world scenario, a long and random is preferred.
     */
	private String pepper;
	
	public String getPepper() {	return this.pepper;	}
	public void setPepper(String pepper) { this.pepper = pepper; }
}
