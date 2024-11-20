package com.example.demo.controllers;

import com.example.demo.models.response.UserResponse;
import com.example.demo.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get")
    public ResponseEntity<UserResponse> getUser() {
        return userService.getUser();
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateUser() {
        return null;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser() {
        return null;
    }

}
