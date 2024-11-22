package com.example.demo.services.impl;

import com.example.demo.models.UserRole;
import com.example.demo.models.dto.UserDTO;
import com.example.demo.models.request.TokenRefreshRequest;
import com.example.demo.models.request.user.UserLoginRequest;
import com.example.demo.models.response.TokenRefreshResponse;
import com.example.demo.models.response.user.UserLoginResponse;
import com.example.demo.services.UserService;
import com.example.demo.util.TokenUtil;
import com.example.demo.util.TokenUtilTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class AuthServiceImplTest {
    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenUtil tokenUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void login_JustWorkTest() {
        // Arrange
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("testPass");

        UserDTO userDTO = new UserDTO();
        userDTO.setId("123");
        userDTO.setUsername("testUser");
        userDTO.setRole(UserRole.USER);

        when(userService.getUserByUsername("testUser")).thenReturn(Optional.of(userDTO));

        // Act
        ResponseEntity<UserLoginResponse> response = authService.login(loginRequest);

        // Assert
        assertNotNull(response.getBody());

        boolean result = response.getBody().isResult();
        int statusCode = response.getStatusCode().value();
        final int statusCodeOk = 200;

        assertTrue(result);
        assertEquals(statusCode, statusCodeOk);
        verify(tokenUtil).generateAccessToken(userDTO.getId(), userDTO.getRole());
        verify(tokenUtil).generateRefreshToken(userDTO.getId());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void login_WithNoUserInDB_BodyWithBadStatusCodeAndNullFields() {
        // Arrange
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("testPass");

        when(userService.getUserByUsername("testUser")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<UserLoginResponse> response = authService.login(loginRequest);

        // Assert
        assertNotNull(response.getBody());

        final int statusCodeBad = 400;

        assertFalse(response.getBody().isResult());
        assertEquals(statusCodeBad, response.getStatusCode().value());
        assertNull(response.getBody().getAccessToken());
        assertNull(response.getBody().getRefreshToken());
    }

    @Test
    void login_WithBadRequestData_BodyWithBadStatusCodeAndNullFields() {
        // Arrange
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setUsername(null);
        loginRequest.setPassword(null);

        // Act && Assert
        assertDoesNotThrow(() -> authService.login(loginRequest));
        ResponseEntity<UserLoginResponse> response = authService.login(loginRequest);

        assertNotNull(response.getBody());
        boolean result = response.getBody().isResult();
        int statusCode = response.getStatusCode().value();
        final int statusCodeBad = 400;

        assertFalse(result);
        assertEquals(statusCode, statusCodeBad);
        assertNull(response.getBody().getAccessToken());
        assertNull(response.getBody().getRefreshToken());
    }

    @Test
    void refreshToken_ValidRefreshToken_ReturnsNewAccessToken() {
        // Arrange
        TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest();
        tokenRefreshRequest.setRefreshToken("validRefreshToken");

        String userId = "456";
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setUsername("testUser");
        userDTO.setRole(UserRole.USER);

        when(tokenUtil.isTokenValid("validRefreshToken")).thenReturn(true);
        when(tokenUtil.getUserId("validRefreshToken")).thenReturn(userId);
        when(userService.getUserById(userId)).thenReturn(Optional.of(userDTO));

        // Act
        ResponseEntity<TokenRefreshResponse> response = authService.refreshToken(tokenRefreshRequest);

        // Assert
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCode().value());
        verify(tokenUtil).generateAccessToken(userId, UserRole.USER);
    }

    @Test
    void refreshToken_InvalidRefreshToken_ReturnsBadRequest() {
        TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest();
        tokenRefreshRequest.setRefreshToken("invalidRefreshToken");

        when(tokenUtil.isTokenValid("invalidRefreshToken")).thenReturn(false);

        ResponseEntity<TokenRefreshResponse> response = authService.refreshToken(tokenRefreshRequest);

        assertNotNull(response.getBody());
        assertEquals(400, response.getStatusCode().value());
        assertNull(response.getBody().getAccessToken());
        verify(tokenUtil, never()).generateAccessToken(any(), any());
    }

    @Test
    void refreshToken_UserNotFound_ReturnsBadRequest() {
        // Arrange
        TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest();
        tokenRefreshRequest.setRefreshToken("validRefreshToken");

        String userId = "456";
        when(tokenUtil.isTokenValid("validRefreshToken")).thenReturn(true);
        when(tokenUtil.getUserId("validRefreshToken")).thenReturn(userId);
        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<TokenRefreshResponse> response = authService.refreshToken(tokenRefreshRequest);

        // Assert
        assertNotNull(response.getBody());
        assertEquals(400, response.getStatusCode().value());
        assertNull(response.getBody().getAccessToken());
    }
}
