package org.example.authservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.authservice.exception.UserNotFound;
import org.example.authservice.models.request.TokenRefreshRequest;
import org.example.authservice.models.request.user.UserLoginRequest;
import org.example.authservice.models.request.user.UserRegistrationRequest;
import org.example.authservice.models.response.TokenRefreshResponse;
import org.example.authservice.models.response.user.UserLoginResponse;
import org.example.authservice.models.response.user.UserRegistrationResponse;
import org.example.authservice.services.AuthService;
import org.example.authservice.services.UserService;
import org.example.authservice.util.TokenUtil;
import org.example.dtolib.dto.user.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final TokenUtil tokenUtil;
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;


    @Override
    public ResponseEntity<UserLoginResponse> login(UserLoginRequest userLoginRequest) {
        try {
            Optional<UserDTO> userDTO = userService.getUserByUsername(userLoginRequest.getUsername());

            if (userDTO.isEmpty()) {
                throw new UserNotFound("Пользователь не был найден!");
            }

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginRequest.getUsername(),
                            userLoginRequest.getPassword()
                    )
            );

            UserDTO user = userDTO.orElseThrow(
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
    public ResponseEntity<UserRegistrationResponse> registration(UserRegistrationRequest userRegistrationRequest) {
        try {
            if (!userRegistrationRequest.getPassword().equals(userRegistrationRequest.getConfirmPassword())) {
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

            Optional<UserDTO> optionalUserDTO = userService.getUserByUsername(userRegistrationRequest.getUsername());

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

            userRegistrationRequest.setPassword(passwordEncoder.encode(userRegistrationRequest.getPassword()));

            userService.saveUser(
                    UserDTO
                            .builder()
                            .username(userRegistrationRequest.getUsername())
                            .password(userRegistrationRequest.getPassword())
                            .firstName(userRegistrationRequest.getFirstName())
                            .lastName(userRegistrationRequest.getLastName())
                            .role(userRegistrationRequest.getRole())
                            .number(userRegistrationRequest.getNumber())
                            .build()

            );

            UserDTO user = userService.getUserByUsername(
                    userRegistrationRequest.getUsername()
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

            UserDTO user = userService
                    .getUserById(
                            tokenUtil.getUserId(
                                    tokenRefreshRequest.getRefreshToken()
                            )
                    ).orElseThrow(
                            () -> new UserNotFound("Пользователь не был найден")
                    );
            String accessToken = tokenUtil.generateAccessToken(user.getId(), user.getRole());

            return ResponseEntity
                    .ok()
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
