package org.example.authservice.models.request.user;

import org.example.authservice.models.entity.UserEntity;
import org.example.dtolib.dto.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
public class UserRegistrationRequestTest {

    @Test
    public void toEntity_justWorks() {
        String firstName = "firstName";
        String lastName = "lastName";
        String number = "123456789";
        String username = "2131231";
        String password = "password";
        String confirmPassword = "confirmPassword";
        UserRole role = UserRole.USER;

        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest(
                firstName, lastName, number, username, password, confirmPassword, role
        );

        UserEntity userEntity = UserRegistrationRequest.toEntity(userRegistrationRequest);

        UserEntity rightUserEntity = new UserEntity();

        rightUserEntity.setFirstName(firstName);
        rightUserEntity.setLastName(lastName);
        rightUserEntity.setNumber(number);
        rightUserEntity.setUsername(username);
        rightUserEntity.setPassword(password);
        rightUserEntity.setRole(role);

        assertEquals(rightUserEntity, userEntity);
    }

    @Test
    public void toEntity_withNullRequest() {
        UserRegistrationRequest userRegistrationRequest = null;

        assertDoesNotThrow(() -> UserRegistrationRequest.toEntity(userRegistrationRequest));

        UserEntity userEntity = UserRegistrationRequest.toEntity(userRegistrationRequest);

        assertEquals(null, userEntity);
    }

    @Test
    public void toEntity_roleAdminDowngradeToUser() {
        String firstName = "firstName";
        String lastName = "lastName";
        String number = "123456789";
        String username = "2131231";
        String password = "password";
        String confirmPassword = "confirmPassword";
        UserRole role = UserRole.USER;

        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest(
                firstName, lastName, number, username, password, confirmPassword, role
        );

        UserEntity userEntity = UserRegistrationRequest.toEntity(userRegistrationRequest);

        UserEntity rightUserEntity = new UserEntity();

        rightUserEntity.setFirstName(firstName);
        rightUserEntity.setLastName(lastName);
        rightUserEntity.setNumber(number);
        rightUserEntity.setUsername(username);
        rightUserEntity.setPassword(password);
        rightUserEntity.setRole(UserRole.USER);

        assertEquals(rightUserEntity, userEntity);
    }
}
