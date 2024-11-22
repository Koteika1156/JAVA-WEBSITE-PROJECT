package com.example.demo.services.impl;

import com.example.demo.models.entity.UserEntity;
import com.example.demo.models.request.user.UserUpdateRequest;
import com.example.demo.models.response.user.UserDeleteResponse;
import com.example.demo.models.response.user.UserResponse;
import com.example.demo.models.response.user.UserUpdateResponse;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserServiceImpl userService;

    final int okStatusCode = 200;
    final int badStatusCode = 400;

    final String userId = "mockUser";

    @BeforeEach
    void setUp() {
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.getName()).thenReturn(userId);

        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);

        // Устанавливаем моки в SecurityContextHolder
        SecurityContextHolder.setContext(mockSecurityContext);
    }

    @Test
    void testGetUser() {
        UserEntity userEntity = new UserEntity();

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        ResponseEntity<UserResponse> response = userService.getUser();

        assertEquals(okStatusCode, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetUser_NotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseEntity<UserResponse> response = userService.getUser();

        assertEquals(badStatusCode, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void testUpdateUser_Success() {
        UserUpdateRequest updateRequest = new UserUpdateRequest("newPassword", "newPassword", "newFirstName", "newLastName", "newNumber");

        UserEntity userEntity = new UserEntity();
        userEntity.setPassword("oldPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(ArgumentMatchers.any(UserEntity.class))).thenReturn(userEntity);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");

        ResponseEntity<UserUpdateResponse> response = userService.updateUser(updateRequest);

        assertNotNull(response.getBody());
        assertEquals(okStatusCode, response.getStatusCode().value());
        assertTrue(response.getBody().isResult());
        assertNotNull(response.getBody().getMessage());
    }

    @Test
    void testUpdateUser_PasswordMismatch() {
        UserUpdateRequest updateRequest = new UserUpdateRequest("newFirstName", "newLastName", "newNumber", "newPassword", "differentPassword");

        ResponseEntity<UserUpdateResponse> response = userService.updateUser(updateRequest);

        assertNotNull(response.getBody());
        assertEquals(badStatusCode, response.getStatusCode().value());
        assertFalse(response.getBody().isResult());
        assertNotNull(response.getBody().getMessage());
    }

    @Test
    void testUpdateUser_NotFound() {
        UserUpdateRequest updateRequest = new UserUpdateRequest("newFirstName", "newLastName", "newNumber", "newPassword", "newPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseEntity<UserUpdateResponse> response = userService.updateUser(updateRequest);

        assertNotNull(response.getBody());
        assertEquals(badStatusCode, response.getStatusCode().value());
        assertFalse(response.getBody().isResult());
        assertNotNull(response.getBody().getMessage());
    }

    @Test
    void testDeleteUser_Success() {
        doNothing().when(userRepository).deleteById(userId);

        ResponseEntity<UserDeleteResponse> response = userService.deleteUser();

        assertNotNull(response.getBody());
        assertEquals(okStatusCode, response.getStatusCode().value());
        assertTrue(response.getBody().isResult());
        assertNotNull(response.getBody().getMessage());
    }

    @Test
    void testDeleteUser_Failure() {
        doThrow(new RuntimeException("Delete error")).when(userRepository).deleteById(userId);

        ResponseEntity<UserDeleteResponse> response = userService.deleteUser();

        assertNotNull(response.getBody());
        assertEquals(badStatusCode, response.getStatusCode().value());
        assertFalse(response.getBody().isResult());
        assertNotNull(response.getBody().getMessage());
    }
}
