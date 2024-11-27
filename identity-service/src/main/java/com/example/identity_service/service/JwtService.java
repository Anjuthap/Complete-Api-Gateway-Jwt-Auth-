package com.example.identity_service.service;

import com.example.identity_service.entity.AuthenticationRequest;
import com.example.identity_service.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsServer userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    public String createJwtToken(AuthenticationRequest authenticationRequest) throws Exception {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        return jwtUtil.generateToken(userDetails);
    }
}