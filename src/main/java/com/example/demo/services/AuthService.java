package com.example.demo.services;

import com.example.demo.models.request.UserLoginRequest;
import com.example.demo.models.request.UserRegistrationRequest;
import com.example.demo.models.response.UserLoginResponse;
import com.example.demo.models.response.UserRegistrationResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<UserLoginResponse> login(UserLoginRequest userLoginRequest);
    ResponseEntity<UserRegistrationResponse> registration(UserRegistrationRequest userLoginRequest);
}
