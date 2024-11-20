package com.example.demo.services;

import com.example.demo.models.request.TokenRefreshRequest;
import com.example.demo.models.request.user.UserLoginRequest;
import com.example.demo.models.request.user.UserRegistrationRequest;
import com.example.demo.models.response.TokenRefreshResponse;
import com.example.demo.models.response.user.UserLoginResponse;
import com.example.demo.models.response.user.UserRegistrationResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<UserLoginResponse> login(UserLoginRequest userLoginRequest);

    ResponseEntity<UserRegistrationResponse> registration(UserRegistrationRequest userLoginRequest);

    ResponseEntity<TokenRefreshResponse> refreshToken(TokenRefreshRequest tokenRefreshRequest);
}