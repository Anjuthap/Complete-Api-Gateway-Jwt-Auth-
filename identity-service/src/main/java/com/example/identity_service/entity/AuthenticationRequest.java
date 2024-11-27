package com.example.identity_service.entity;

import lombok.Data;

@Data
public class AuthenticationRequest {

    private String username;
    private String password;
}