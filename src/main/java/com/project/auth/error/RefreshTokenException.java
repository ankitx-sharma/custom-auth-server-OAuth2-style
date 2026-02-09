package com.project.auth.error;

public class RefreshTokenException extends RuntimeException {

	private static final long serialVersionUID = -1920465186462854022L;
	private String error;
	private String description;
	
	public RefreshTokenException(String error, String description) {
		super(description);
		this.error = error;
		this.description = description;
	}
	
	public String getError() { return this.error; }
	public String getDescription() { return this.description; }
}
