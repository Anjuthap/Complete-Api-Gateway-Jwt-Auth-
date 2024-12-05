package com.example.identity_service.service;

import com.example.identity_service.entity.User;
import com.example.identity_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    public User save(User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        return userRepository.save(user);
//    }
//    public User findByUsername(String username) {
//        return userRepository.findByUsername(username);
//    }
    @Autowired
private JwtService jwtService;

    public User saveUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
       return userRepository.save(user);
    }
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public String generateToken(User user, Authentication authentication){
        return jwtService.generateToken(user, authentication);
    }
    public void validateToken(String token){
        jwtService.validateToken(token);
    }

}