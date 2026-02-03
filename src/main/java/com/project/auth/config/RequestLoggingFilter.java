package com.project.auth.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RequestLoggingFilter extends OncePerRequestFilter{
	
	private static final Logger logger = LoggerFactory.getLogger("http");

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		long start = System.currentTimeMillis();
		
		try {
			filterChain.doFilter(request, response);
		} finally {
			long ms = System.currentTimeMillis() - start;
			String path = request.getRequestURI();
			String method = request.getMethod();
			int status = response.getStatus();
			
			logger.info("{} {} status={} duration={}", method, path, status, ms);
		}
		
	}

}
