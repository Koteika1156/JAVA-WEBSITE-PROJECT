package com.example.demo.services.impl;

import com.example.demo.exception.UserNotFound;
import com.example.demo.models.entity.UserEntity;
import com.example.demo.models.request.user.UserUpdateRequest;
import com.example.demo.models.response.user.UserDeleteResponse;
import com.example.demo.models.response.user.UserResponse;
import com.example.demo.models.response.user.UserUpdateResponse;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.example.dtolib.dto.user.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Qualifier("baseImplUserService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final PasswordEncoder passwordEncoder;

    public Optional<UserEntity> getUserEntityById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<UserDTO> getUserByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);

        return Optional.ofNullable(
                UserDTO
                        .builder()
                        .id(userEntity.getId())
                        .username(userEntity.getUsername())
                        .password(userEntity.getPassword())
                        .firstName(userEntity.getFirstName())
                        .lastName(userEntity.getLastName())
                        .role(userEntity.getRole())
                        .build()
        );
    }

    @Override
    public Optional<UserDTO> getUserById(String id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);

        return userEntity.map(entity -> UserDTO
                .builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .role(entity.getRole())
                .build());

    }

    @Override
    public void saveUser(UserEntity user) {
        userRepository.save(user);
    }

    @Override
    public ResponseEntity<UserResponse> getUser() {
        try {
            String id = SecurityContextHolder.getContext().getAuthentication().getName();

            UserEntity user = getUserEntityById(id).orElseThrow(
                    () -> new UserNotFound("Пользователь не был найден!")
            );
            return ResponseEntity.ok(UserResponse.toResponse(user));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body(
                            UserResponse.builder()
                                    .message("Ошибка получения пользователя!")
                                    .build()
                    );
        }
    }

    @Override
    public ResponseEntity<UserUpdateResponse> updateUser(UserUpdateRequest userUpdateRequest) {
        try {
            if (userUpdateRequest.getNewPassword() != null && userUpdateRequest.getNewPasswordConfirm() != null &&
                    !userUpdateRequest.getNewPassword().equals(userUpdateRequest.getNewPasswordConfirm())
            ) {
                return ResponseEntity
                        .badRequest()
                        .body(
                                UserUpdateResponse
                                        .builder()
                                        .result(false)
                                        .message("Пароли не совпадают!")
                                        .build()
                        );
            }
            String id = SecurityContextHolder.getContext().getAuthentication().getName();

            UserEntity user = userRepository.findById(id).orElseThrow(
                    () -> new UserNotFound("Пользователь не найден!")
            );

            if (userUpdateRequest.getNewPassword() != null) {
                user.setPassword(passwordEncoder.encode(userUpdateRequest.getNewPassword()));
            }
            if (userUpdateRequest.getFirstName() != null) {
                user.setFirstName(userUpdateRequest.getFirstName());
            }
            if (userUpdateRequest.getLastName() != null) {
                user.setLastName(userUpdateRequest.getLastName());
            }
            if (userUpdateRequest.getNumber() != null) {
                user.setNumber(userUpdateRequest.getNumber());
            }

            userRepository.save(user);

            return ResponseEntity
                    .ok()
                    .body(
                            UserUpdateResponse
                                    .builder()
                                    .result(true)
                                    .message("Пользователь успешно обновлен")
                                    .build()
                    );
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body(
                            UserUpdateResponse
                                    .builder()
                                    .result(false)
                                    .message("Ошибка при обновлении пользователя")
                                    .build()
                    );
        }
    }

    @Override
    public ResponseEntity<UserDeleteResponse> deleteUser() {
        try {
            String id = SecurityContextHolder.getContext().getAuthentication().getName();

            userRepository.deleteById(id);

            return ResponseEntity
                    .ok()
                    .body(
                            UserDeleteResponse
                                    .builder()
                                    .result(true)
                                    .message("Пользователь успешно удален")
                                    .build()
                    );
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body(
                            UserDeleteResponse
                                    .builder()
                                    .result(false)
                                    .message("Ошибка при удалении пользователя")
                                    .build()
                    );
        }
    }
}
