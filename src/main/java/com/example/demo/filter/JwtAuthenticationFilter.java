package com.example.demo.filter;

import com.example.demo.models.UserRole;
import com.example.demo.services.impl.AuthServiceImpl;
import com.example.demo.util.TokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    JwtAuthenticationFilter(TokenUtil tokenUtil) {
        this.jwtUtil = tokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String userId = null;
        UserRole role = null;
        String jwt = null;

        String bearerSuffix = "Bearer ";

        if (header != null && header.startsWith("Bearer ")) {
            jwt = header.substring("Bearer ".length());
            try {
                userId = jwtUtil.getUserId(jwt);
                role = jwtUtil.getUserRole(jwt);
            } catch (Exception e) {
                logger.debug("Token died");
            }
        }

        logger.info("JWT Token: {}", jwt);
        logger.info("User ID: " + userId);
        logger.info("Role: " + role);

        if (userId != null && role != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    List.of(new SimpleGrantedAuthority(String.format("ROLE_%s", role)))
            );
            SecurityContextHolder.getContext().setAuthentication(token);
        }

        chain.doFilter(request, response);
    }
}
