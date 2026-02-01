package com.project.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration for the application.
 */
@Configuration
public class SecurityConfig {

	/**
	 * Provides a {@link PasswordEncoder} bean using BCrypt hashing.
	 * 
	 * @return a {@link BCryptPasswordEncoder} instance
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/**
	 * Configures the main Spring Security filter chain.
	 * 
	 *<ul>
	 *  <li>{@code /api/.well-known/jwks.json} – JWKS endpoint for JWT verification</li>
	 *</ul>
	 *
	 * @param http the {@link HttpSecurity} to configure
	 * @return the configured {@link SecurityFilterChain}
	 * @throws Exception if a security configuration error occurs
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.securityMatcher("/**")
				.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(request -> 
					request.requestMatchers("/api/.well-known/jwks.json", "/debug/token").permitAll()
					.anyRequest().authenticated())
				.build();
	}
}
