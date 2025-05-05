package com.example.todoj.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Value("${auth.service.url}")
    private String authServiceUrl;

    private final RestTemplate restTemplate;
    private final JwtUtils jwtUtils;

    public JwtAuthenticationFilter(RestTemplate restTemplate, JwtUtils jwtUtils) {
        this.restTemplate = restTemplate;
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null) {
                logger.debug("JWT token found in request");
                
                // First try local validation
                boolean localValidation = jwtUtils.validateToken(jwt);
                logger.debug("Local JWT validation result: {}", localValidation);
                
                if (localValidation) {
                    Authentication authentication = jwtUtils.getAuthentication(jwt);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.debug("Authentication set in SecurityContext using local validation");
                } 
                // If local validation fails, try validating with auth service
                else {
                    logger.debug("Local validation failed, trying auth service validation");
                    boolean remoteValidation = validateJwtTokenWithAuthService(jwt);
                    logger.debug("Remote JWT validation result: {}", remoteValidation);
                    
                    if (remoteValidation) {
                        String username = jwtUtils.getUsernameFromToken(jwt);
                        Authentication authentication = jwtUtils.getAuthentication(jwt);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        logger.debug("Authentication set in SecurityContext using remote validation");
                    } else {
                        logger.warn("Both local and remote token validation failed");
                    }
                }
            } else {
                logger.debug("No JWT token found in request");
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
            if (e.getCause() != null) {
                logger.error("Caused by: {}", e.getCause().getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }

    private boolean validateJwtTokenWithAuthService(String token) {
        try {
            logger.info("Attempting to validate token with auth service at: {}", authServiceUrl);
            
            // Call auth service to validate token
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            headers.set("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            String validateUrl = authServiceUrl + "/api/auth/validate";
            logger.debug("Sending request to: {}", validateUrl);
            
            ResponseEntity<?> response = restTemplate.exchange(
                    validateUrl,
                    HttpMethod.GET,
                    entity,
                    Object.class);
            
            logger.info("Token validation response status: {}", response.getStatusCode());
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            logger.error("Token validation error: {}", e.getMessage());
            logger.error("Token validation exception type: {}", e.getClass().getName());
            if (e.getCause() != null) {
                logger.error("Caused by: {}", e.getCause().getMessage());
            }
            return false;
        }
    }
}