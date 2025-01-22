package org.example.authservice.services.impl;

import org.example.authservice.config.RabbitMQConfig;
import org.example.authservice.services.UserService;
import org.example.dtolib.dto.request.MqUserRegistrationRequest;
import org.example.dtolib.dto.request.MqUserRequest;
import org.example.dtolib.dto.response.MqUserResponse;
import org.example.dtolib.dto.user.UserDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceRabbitMQ implements UserService {

    private final RabbitTemplate rabbitTemplate;

    public UserServiceRabbitMQ(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public Optional<UserDTO> getUserByUsername(String username) {
        MqUserRequest userRequest = new MqUserRequest(username, "getUserByUsername");
        MqUserResponse userResponse = (MqUserResponse) rabbitTemplate.convertSendAndReceive(
                RabbitMQConfig.USER_REQUEST_QUEUE,
                userRequest
        );

        if (userResponse != null && userResponse.isSuccess()) {
            return Optional.of(userResponse.getUserDTO());
        }

        return Optional.empty();
    }

    public Optional<UserDTO> getUserById(String username) {
        MqUserRequest userRequest = new MqUserRequest(username, "getUserById");
        MqUserResponse userResponse = (MqUserResponse) rabbitTemplate.convertSendAndReceive(
                RabbitMQConfig.USER_REQUEST_QUEUE,
                userRequest
        );

        if (userResponse != null && userResponse.isSuccess()) {
            return Optional.of(userResponse.getUserDTO());
        }

        return Optional.empty();
    }

    @Override
    public void saveUser(UserDTO user) {
        MqUserRegistrationRequest userRequest = new MqUserRegistrationRequest(user);
        MqUserResponse userResponse = (MqUserResponse) rabbitTemplate.convertSendAndReceive(
                RabbitMQConfig.USER_REG_REQUEST_QUEUE,
                userRequest
        );

        if (userResponse == null || !userResponse.isSuccess()) {
            throw new RuntimeException("Failed to save user");
        }
    }
}