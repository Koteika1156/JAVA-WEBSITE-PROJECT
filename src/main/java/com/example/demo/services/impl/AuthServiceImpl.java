package com.example.demo.services.impl;

import com.example.demo.config.SecurityConfig;
import com.example.demo.exception.UserNotFound;
import com.example.demo.models.entity.UserEntity;
import com.example.demo.models.request.UserLoginRequest;
import com.example.demo.models.request.UserRegistrationRequest;
import com.example.demo.models.response.UserLoginResponse;
import com.example.demo.models.response.UserRegistrationResponse;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.AuthService;
import com.example.demo.util.TokenUtil;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final TokenUtil tokenUtil;
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    AuthServiceImpl(UserRepository userRepository, TokenUtil tokenUtil) {
        this.userRepository = userRepository;
        this.tokenUtil = tokenUtil;
    }

    @Override
    public ResponseEntity<UserLoginResponse> login(UserLoginRequest userLoginRequest) {
        return null;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<UserRegistrationResponse> registration(UserRegistrationRequest userLoginRequest) {
        try {
            if (!userLoginRequest.getPassword().equals(userLoginRequest.getConfirmPassword())) {
                return ResponseEntity
                        .badRequest()
                        .body(
                                UserRegistrationResponse
                                        .builder()
                                        .accessToken(null)
                                        .refreshToken(null)
                                        .result(false)
                                        .build()
                        );
            }

            userLoginRequest.setPassword(SecurityConfig.passwordEncoder().encode(userLoginRequest.getPassword()));
            userRepository.save(UserRegistrationRequest.toEntity(userLoginRequest));

            UserEntity user = userRepository.findByUsername(userLoginRequest.getUsername()).orElseThrow(
                    () -> new UserNotFound("Пользователь не был найдет")
            );

            String accessToken = tokenUtil.generateAccessToken(user.getId(), user.getRole());
            String refreshToken = tokenUtil.generateRefreshToken(user.getId());

            return ResponseEntity
                    .ok(
                            UserRegistrationResponse
                                    .builder()
                                    .accessToken(accessToken)
                                    .refreshToken(refreshToken)
                                    .result(true)
                                    .build()
                    );
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body(
                            UserRegistrationResponse
                                    .builder()
                                    .accessToken(null)
                                    .refreshToken(null)
                                    .result(false)
                                    .build()
                    );
        }
    }
}