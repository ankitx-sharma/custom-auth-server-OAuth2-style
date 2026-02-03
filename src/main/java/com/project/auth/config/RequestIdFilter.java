package com.project.auth.config;

import java.io.IOException;
import java.util.UUID;

import org.jboss.logging.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestIdFilter extends OncePerRequestFilter{

	private static final String HEADER = "X-Request-Id";
	private static final String MDC_KEY = "requestId";
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String requestId = request.getHeader(HEADER);
		if(requestId == null || requestId.isBlank()) {
			requestId = UUID.randomUUID().toString();
		}
		
		MDC.put(MDC_KEY, requestId);
		response.setHeader(HEADER, requestId);
		
		try {
			filterChain.doFilter(request, response);
		} finally {
			MDC.remove(MDC_KEY);
		}
		
	}

}
