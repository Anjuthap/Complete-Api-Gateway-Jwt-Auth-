package com.example.api_gateway.exception;

public class RoleNotMatchedException extends RuntimeException{
    public RoleNotMatchedException(String message) {
        super(message);
    }
}
