package com.project.auth.error;

import org.springframework.http.HttpStatus;

public class OAuthErrorException extends RuntimeException{
	private static final long serialVersionUID = 813732494339770715L;
	
	private final HttpStatus status;
	private final String error;
	private final String description;
	
	public OAuthErrorException(HttpStatus status, String error, String description) {
		super(description);
		this.status = status;
		this.error = error;
		this.description = description;
	}

	public HttpStatus getStatus() { return status; }
	public String getError() { return error; }
	public String getDescription() { return description; }
}
