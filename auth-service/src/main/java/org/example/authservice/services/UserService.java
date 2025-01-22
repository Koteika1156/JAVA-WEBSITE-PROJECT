package org.example.authservice.services;

import org.example.dtolib.dto.user.UserDTO;

import java.util.Optional;

public interface UserService {
    Optional<UserDTO> getUserByUsername(String username);

    Optional<UserDTO> getUserById(String id);

    void saveUser(UserDTO user);
}

