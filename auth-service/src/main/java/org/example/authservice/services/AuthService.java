package org.example.authservice.services;

import org.example.authservice.models.request.TokenRefreshRequest;
import org.example.authservice.models.request.user.UserLoginRequest;
import org.example.authservice.models.request.user.UserRegistrationRequest;
import org.example.authservice.models.response.TokenRefreshResponse;
import org.example.authservice.models.response.user.UserLoginResponse;
import org.example.authservice.models.response.user.UserRegistrationResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<UserLoginResponse> login(UserLoginRequest userLoginRequest);

    ResponseEntity<UserRegistrationResponse> registration(UserRegistrationRequest userLoginRequest);

    ResponseEntity<TokenRefreshResponse> refreshToken(TokenRefreshRequest tokenRefreshRequest);
}
