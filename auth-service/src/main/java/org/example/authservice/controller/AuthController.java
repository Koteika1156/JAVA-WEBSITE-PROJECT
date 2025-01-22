package org.example.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.authservice.models.request.TokenRefreshRequest;
import org.example.authservice.models.request.user.UserLoginRequest;
import org.example.authservice.models.request.user.UserRegistrationRequest;
import org.example.authservice.models.response.TokenRefreshResponse;
import org.example.authservice.models.response.user.UserLoginResponse;
import org.example.authservice.models.response.user.UserRegistrationResponse;
import org.example.authservice.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

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