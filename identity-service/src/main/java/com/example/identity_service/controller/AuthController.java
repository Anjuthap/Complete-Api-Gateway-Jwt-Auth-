package com.example.identity_service.controller;

import com.example.identity_service.entity.User;
import com.example.identity_service.entity.AuthenticationRequest;
import com.example.identity_service.entity.AuthenticationResponse;
import com.example.identity_service.service.JwtService;
import com.example.identity_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

//    @Autowired
//    private JwtService jwtService;

    @Autowired
    private AuthService service;

    @Autowired
    private AuthenticationManager authenticationManager;

//    @PostMapping("/login")
//    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
//        final String jwt = jwtService.generateToken(authenticationRequest);
//        return ResponseEntity.ok(new AuthenticationResponse(jwt));
//    }

    @PostMapping("/signup")
    public String  registerUser(@RequestBody User user) {

        if (service.findByUsername(user.getUsername()).isPresent()) {
            return ("Username is already taken.");
        }
        service.saveUser(user);
        return "User registered successfully.";
    }
    @PostMapping("/login")
    public String getToken(@RequestBody AuthenticationRequest authRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            return service.generateToken(authRequest.getUsername());
        } else {
            throw new RuntimeException("invalid access");
        }
    }
    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        service.validateToken(token);
        return "Token is valid";
    }

}