package com.example.demo.controllers;

import com.example.demo.models.request.UserRegistrationRequest;
import com.example.demo.models.response.UserRegistrationResponse;
import com.example.demo.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ModelAndView login(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        return null;
    }

    @PostMapping("/registration")
    public ResponseEntity<UserRegistrationResponse> register(@RequestBody UserRegistrationRequest registrationRequest) {
        return authService.registration(registrationRequest);
    }

}