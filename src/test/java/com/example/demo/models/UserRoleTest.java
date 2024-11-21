package com.example.demo.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
public class UserRoleTest {

    @Test
    public void fromValue_allValidRolesParsed() {
        assertDoesNotThrow(() -> UserRole.fromValue("admin"));
        assertDoesNotThrow(() -> UserRole.fromValue("user"));
    }

    @Test
    public void fromValue_invalidRole_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> UserRole.fromValue("moderatorrrr"));
    }
}
