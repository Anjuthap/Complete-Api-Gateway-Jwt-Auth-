package com.example.user_service.controller;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

        @GetMapping("/view/bank")
        public String greeting() {
            return "Can  be accessed by both(bank view)!";
        }

        @PostMapping("/admin/create/bank")
        public String create() {
            return "Can be accessed by admin(create bank)!";
        }

        @PutMapping("/admin/update/bank")
        public String update() {
            return "Can only be accessed by admin(update bank)!";
    }

        @GetMapping("/admin/view/bank")
        public String feedback(){
            return "Fedback can be viewed ";
    }
    @PutMapping("/update/logo")
    public String updateLogo() {
        return "Can only be accessed by role (update)!";
    }
    }

