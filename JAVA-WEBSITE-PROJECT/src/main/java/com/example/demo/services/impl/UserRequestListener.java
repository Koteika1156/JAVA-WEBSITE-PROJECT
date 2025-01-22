package com.example.demo.services.impl;

import com.example.demo.config.RabbitMQConfig;
import com.example.demo.models.entity.UserEntity;
import com.example.demo.services.UserService;
import org.example.dtolib.dto.request.MqUserRegistrationRequest;
import org.example.dtolib.dto.request.MqUserRequest;
import org.example.dtolib.dto.response.MqUserResponse;
import org.example.dtolib.dto.user.UserDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRequestListener {

    private final UserService userService;

    public UserRequestListener(UserService userService) {
        this.userService = userService;
    }

    @RabbitListener(queues = RabbitMQConfig.USER_REQUEST_QUEUE)
    public MqUserResponse handleUserRequest(MqUserRequest userRequest) {
        try {
            if ("getUserByUsername".equals(userRequest.getAction())) {
                Optional<UserDTO> user = userService.getUserByUsername(userRequest.getValue());
                if (user.isPresent()) {
                    return new MqUserResponse(true, user.get());
                }
            }
            if ("getUserById".equals(userRequest.getAction())) {
                Optional<UserDTO> user = userService.getUserById(userRequest.getValue());
                if (user.isPresent()) {
                    return new MqUserResponse(true, user.get());
                }
            }
            return new MqUserResponse(false, null);
        } catch (Exception e) {
            return new MqUserResponse(false, null);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.USER_REG_REQUEST_QUEUE)
    public MqUserResponse handleUserRequest(MqUserRegistrationRequest userRequest) {
        try {
            if(userRequest == null || userRequest.getValue() == null) {
                return new MqUserResponse(false, null);
            }

            userService.saveUser(
                    UserEntity
                            .builder()
                            .number(userRequest.getValue().getNumber())
                            .username(userRequest.getValue().getUsername())
                            .password(userRequest.getValue().getPassword())
                            .firstName(userRequest.getValue().getFirstName())
                            .lastName(userRequest.getValue().getLastName())
                            .role(userRequest.getValue().getRole())
                            .build()
            );

            return new MqUserResponse(true, null);
        } catch (Exception e) {
            return new MqUserResponse(false, null);
        }
    }
}