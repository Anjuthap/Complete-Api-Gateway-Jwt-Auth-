package com.example.user_service.controller;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")

public class UserController {

        @GetMapping("/greeting")
        @PreAuthorize("hasRole('USER')")
        public String greeting() {
            return "Hello from User Service!";
        }
    }

