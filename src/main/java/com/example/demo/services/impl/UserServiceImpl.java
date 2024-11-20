package com.example.demo.services.impl;

import com.example.demo.exception.UserNotFound;
import com.example.demo.models.dto.UserDTO;
import com.example.demo.models.entity.UserEntity;
import com.example.demo.models.response.UserResponse;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserEntity> getUserEntityById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<UserDTO> getUserByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);

        return Optional.of(
                UserDTO.toDTO(userEntity)
        );
    }

    @Override
    public Optional<UserDTO> getUserById(String id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);

        return Optional.of(
                UserDTO.toDTO(userEntity.isPresent() ? userEntity.get() : null)
        );
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
                            UserResponse.toResponse(null)
                    );
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO user = getUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User '%s' not found ", username)
        ));

        return new User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(String.format("ROLE_%s", user.getRole())))
        );
    }
}
