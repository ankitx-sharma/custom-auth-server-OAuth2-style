package com.project.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.project.auth.config.JWTProperties;
import com.project.auth.config.RefreshTokenProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = {JWTProperties.class, RefreshTokenProperties.class})
public class UserAuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserAuthServiceApplication.class, args);
	}

}
