package com.example.demo.services;

import com.example.demo.models.entity.UserEntity;
import com.example.demo.models.request.user.UserUpdateRequest;
import com.example.demo.models.response.user.UserDeleteResponse;
import com.example.demo.models.response.user.UserResponse;
import com.example.demo.models.response.user.UserUpdateResponse;
import org.example.dtolib.dto.user.UserDTO;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface UserService {
    Optional<UserDTO> getUserByUsername(String username);

    Optional<UserDTO> getUserById(String id);

    Optional<UserEntity> getUserEntityById(String id);

    void saveUser(UserEntity user);

    ResponseEntity<UserResponse> getUser();

    ResponseEntity<UserUpdateResponse> updateUser(UserUpdateRequest userUpdateRequest);

    ResponseEntity<UserDeleteResponse> deleteUser();
}
