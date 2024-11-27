package com.example.identity_service.filter;

import com.example.identity_service.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.example.identity_service.service.CustomUserDetailsServer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsServer userDetailsService;
    
    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsServer userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
            logger.debug("Extracted JWT: " + jwt);
            logger.debug("Extracted Username: " + username);
        } else {
            logger.debug("Authorization header missing or not starting with 'Bearer '");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtUtil.validateToken(jwt, userDetails)) {
//                    Claims claims = jwtUtil.extractAllClaims(jwt);
//                    List<String> roles = (List<String>) claims.get("roles");
//                    logger.debug("Extracted Roles: " + roles);

//                    if (roles == null || roles.isEmpty()) {
//                        logger.warn("Roles claim is empty or missing.");
//                    }

//                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                            userDetails, null, roles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList())
//                    );
//                    List<SimpleGrantedAuthority> authorities = roles.stream()
//                            .map(role -> new SimpleGrantedAuthority(role))
//                            .collect(Collectors.toList());
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.debug("User authenticated: " + userDetails.getUsername());
                    response.setHeader(HttpHeaders.AUTHORIZATION, jwt);
                } else {
                    logger.warn("Invalid or expired token.");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                    return; // End the request processing if token is invalid
                }
            } catch (Exception e) {
                logger.error("Error processing authentication: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
