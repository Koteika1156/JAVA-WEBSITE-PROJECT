package com.example.demo.services.impl;

import com.example.demo.exception.UserNotFound;
import com.example.demo.models.dto.UserDTO;
import com.example.demo.models.request.TokenRefreshRequest;
import com.example.demo.models.request.UserLoginRequest;
import com.example.demo.models.request.UserRegistrationRequest;
import com.example.demo.models.response.TokenRefreshResponse;
import com.example.demo.models.response.UserLoginResponse;
import com.example.demo.models.response.UserRegistrationResponse;
import com.example.demo.services.AuthService;
import com.example.demo.services.UserService;
import com.example.demo.util.TokenUtil;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final TokenUtil tokenUtil;
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    AuthServiceImpl(
            UserService userService,
            TokenUtil tokenUtil,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.userService = userService;
        this.tokenUtil = tokenUtil;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<UserLoginResponse> login(UserLoginRequest userLoginRequest) {
        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginRequest.getUsername(),
                            userLoginRequest.getPassword()
                    )
            );

            UserDTO user = userService.getUserByUsername(
                    userLoginRequest.getUsername()
            ).orElseThrow(
                    () -> new UserNotFound("Пользователь не был найден!")
            );

            String accessToken = tokenUtil.generateAccessToken(user.getId(), user.getRole());
            String refreshToken = tokenUtil.generateRefreshToken(user.getId());

            return ResponseEntity
                    .ok(
                            UserLoginResponse
                                    .builder()
                                    .accessToken(accessToken)
                                    .refreshToken(refreshToken)
                                    .result(true)
                                    .message("Успешно!")
                                    .build()
                    );
        } catch (UserNotFound e) {
            logger.error(e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body(
                            UserLoginResponse
                                    .builder()
                                    .accessToken(null)
                                    .refreshToken(null)
                                    .result(false)
                                    .message(e.getMessage())
                                    .build()
                    );
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    UserLoginResponse
                            .builder()
                            .accessToken(null)
                            .refreshToken(null)
                            .result(false)
                            .build()

            );
        }
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
                                        .message("Пароли не совпадают!")
                                        .build()
                        );
            }

            Optional<UserDTO> optionalUserDTO = userService.getUserByUsername(userLoginRequest.getUsername());

            if (optionalUserDTO.isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(
                                UserRegistrationResponse
                                        .builder()
                                        .accessToken(null)
                                        .refreshToken(null)
                                        .result(false)
                                        .message("Пользователь с таким username уже существует!")
                                        .build()
                        );
            }

            userLoginRequest.setPassword(passwordEncoder.encode(userLoginRequest.getPassword()));
            userService.saveUser(UserRegistrationRequest.toEntity(userLoginRequest));

            UserDTO user = userService.getUserByUsername(
                    userLoginRequest.getUsername()
            ).orElseThrow(
                    () -> new UserNotFound("Ошбика при сохранении пользователя")
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
                                    .message("Успешно!")
                                    .build()
                    );
        } catch (UserNotFound e) {
            logger.error(e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body(
                            UserRegistrationResponse
                                    .builder()
                                    .accessToken(null)
                                    .refreshToken(null)
                                    .result(false)
                                    .message(e.getMessage())
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
                                    .message("Unknown error")
                                    .build()
                    );
        }
    }

    @Override
    public ResponseEntity<TokenRefreshResponse> refreshToken(TokenRefreshRequest tokenRefreshRequest) {
        try {
            boolean result = tokenUtil.isTokenValid(tokenRefreshRequest.getRefreshToken());

            if (!result) {
                return ResponseEntity
                        .badRequest()
                        .body(
                                TokenRefreshResponse
                                        .builder()
                                        .accessToken(null)
                                        .build()
                        );
            }

            UserDTO user = userService.getUserById(
                    tokenUtil.getUserId(
                            tokenRefreshRequest.getRefreshToken()
                    )
            ).orElseThrow(
                    () -> new UserNotFound("Пользователь не был найден")
            );
            String accessToken = tokenUtil.generateAccessToken(user.getId(), user.getRole());

            return ResponseEntity
                    .badRequest()
                    .body(
                            TokenRefreshResponse
                                    .builder()
                                    .accessToken(accessToken)
                                    .build()
                    );

        } catch (Exception e) {
            logger.error(e.getMessage());

            return ResponseEntity
                    .badRequest()
                    .body(
                            TokenRefreshResponse
                                    .builder()
                                    .accessToken(null)
                                    .build()
                    );
        }
    }
}