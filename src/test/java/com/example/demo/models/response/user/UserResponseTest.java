package com.example.demo.models.response.user;

import com.example.demo.models.UserRole;
import com.example.demo.models.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
public class UserResponseTest {
    @Test
    public void toResponse_justWorks() {
        UserEntity user = new UserEntity();

        String id = "123";
        String firstName = "name";
        String lastName = "lastName";
        String number = "number";
        UserRole role = UserRole.USER;

        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setNumber(number);
        user.setRole(role);

        UserResponse userResponse = UserResponse.toResponse(user);

        UserResponse rightUserResponse = UserResponse.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .number(number)
                .role(role)
                .build();

        assertEquals(rightUserResponse, userResponse);
    }

    @Test
    public void toResponse_withNullEntity() {
        UserEntity user = null;

        assertDoesNotThrow(() -> UserResponse.toResponse(user));

        UserResponse userResponse = UserResponse.toResponse(user);

        assertEquals(null, userResponse);
    }

}
