package com.sweng861.agiletracker.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RateLimitFilter.class);
    private final Map<String, Integer> ipCounts = new HashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (path != null && path.startsWith("/api/")) {
            String ip = request.getRemoteAddr();

            int count = ipCounts.getOrDefault(ip, 0) + 1;
            ipCounts.put(ip, count);

            if (count > 10) {
                logger.warn("Rate limit exceeded for IP: {}", ip);
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("Too Many Requests");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}