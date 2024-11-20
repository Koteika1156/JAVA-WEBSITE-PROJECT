package com.example.demo.services;

import com.example.demo.models.request.TokenRefreshRequest;
import com.example.demo.models.request.UserLoginRequest;
import com.example.demo.models.request.UserRegistrationRequest;
import com.example.demo.models.response.TokenRefreshResponse;
import com.example.demo.models.response.UserLoginResponse;
import com.example.demo.models.response.UserRegistrationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService {
    ResponseEntity<UserLoginResponse> login(UserLoginRequest userLoginRequest);

    ResponseEntity<UserRegistrationResponse> registration(UserRegistrationRequest userLoginRequest);

    ResponseEntity<TokenRefreshResponse> refreshToken(TokenRefreshRequest tokenRefreshRequest);
}