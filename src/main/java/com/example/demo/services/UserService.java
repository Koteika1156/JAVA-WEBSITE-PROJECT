package com.example.demo.services;

import com.example.demo.models.dto.UserDTO;
import com.example.demo.models.entity.UserEntity;
import com.example.demo.models.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    Optional<UserDTO> getUserByUsername(String username);

    Optional<UserDTO> getUserById(String id);

    void saveUser(UserEntity user);

    ResponseEntity<UserResponse> getUser();
/*    ResponseEntity<?> updateUser();

    ResponseEntity<?> deleteUser();*/
}
