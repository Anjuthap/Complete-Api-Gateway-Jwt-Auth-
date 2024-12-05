package com.example.api_gateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RoleNotMatchedException.class)
    public ResponseEntity<Map<String, Object>> handlerRoleNotMatchedException(RoleNotMatchedException roleNotMatchedException) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", roleNotMatchedException.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
    }
}
