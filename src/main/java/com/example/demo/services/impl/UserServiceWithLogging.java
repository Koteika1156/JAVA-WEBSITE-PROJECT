package com.example.demo.services.impl;

import com.example.demo.models.dto.UserDTO;
import com.example.demo.models.entity.UserEntity;
import com.example.demo.models.request.user.UserUpdateRequest;
import com.example.demo.models.response.user.UserDeleteResponse;
import com.example.demo.models.response.user.UserResponse;
import com.example.demo.models.response.user.UserUpdateResponse;
import com.example.demo.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

// Паттерн декоратор структурный, добавляем новый функционал по верх старого
@Service
@Primary
public class UserServiceWithLogging implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceWithLogging.class);
    private final UserService delegate;

    public UserServiceWithLogging(@Qualifier("baseImplUserService") UserService delegate) {
        this.delegate = delegate;
    }

    @Override
    public Optional<UserDTO> getUserByUsername(String username) {
        return delegate.getUserByUsername(username);
    }

    @Override
    public Optional<UserDTO> getUserById(String id) {
        return delegate.getUserById(id);
    }

    @Override
    public Optional<UserEntity> getUserEntityById(String id) {
        return delegate.getUserEntityById(id);
    }

    @Override
    public void saveUser(UserEntity user) {
        delegate.saveUser(user);
    }

    @Override
    public ResponseEntity<UserResponse> getUser() {
        logger.info("Получаем пользователя!");
        ResponseEntity<UserResponse> answer = delegate.getUser();

        if (answer.getStatusCode().value() == 200) {
            logger.info("Пользователь получен!");
        }
        return answer;
    }

    @Override
    public ResponseEntity<UserUpdateResponse> updateUser(UserUpdateRequest userUpdateRequest) {
        logger.info("Обновляем пользователя!");
        ResponseEntity<UserUpdateResponse> answer = delegate.updateUser(userUpdateRequest);

        if (answer.getStatusCode().value() == 200) {
            logger.info("Пользователь обновлен!");
        }
        return answer;
    }

    @Override
    public ResponseEntity<UserDeleteResponse> deleteUser() {
        logger.info("Удаляем пользователя!");
        ResponseEntity<UserDeleteResponse> answer = delegate.deleteUser();

        if (answer.getStatusCode().value() == 200) {
            logger.info("Пользователь удален!");
        }
        return answer;
    }
}
