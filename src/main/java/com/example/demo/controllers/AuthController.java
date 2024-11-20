package com.example.demo.controllers;

import com.example.demo.models.request.TokenRefreshRequest;
import com.example.demo.models.request.UserLoginRequest;
import com.example.demo.models.request.UserRegistrationRequest;
import com.example.demo.models.response.TokenRefreshResponse;
import com.example.demo.models.response.UserLoginResponse;
import com.example.demo.models.response.UserRegistrationResponse;
import com.example.demo.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
        return authService.login(userLoginRequest);
    }

    @PostMapping("/registration")
    public ResponseEntity<UserRegistrationResponse> register(@RequestBody UserRegistrationRequest registrationRequest) {
        return authService.registration(registrationRequest);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@RequestBody TokenRefreshRequest tokenRefreshRequest) {
        return authService.refreshToken(tokenRefreshRequest);
    }
}