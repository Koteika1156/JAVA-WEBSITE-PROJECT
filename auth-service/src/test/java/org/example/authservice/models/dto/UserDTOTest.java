package org.example.authservice.models.dto;

import org.example.authservice.models.entity.UserEntity;
import org.example.dtolib.dto.UserRole;
import org.example.dtolib.dto.user.UserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class UserDTOTest {

    @Test
    public void toDTO_justWorks() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId("123");
        userEntity.setUsername("username");
        userEntity.setPassword("123");
        userEntity.setFirstName("123");
        userEntity.setLastName("lastName");
        userEntity.setRole(UserRole.USER);

        UserDTO userDTO = UserDTO
                .builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .role(userEntity.getRole())
                .build();

        UserDTO rightUserDTO = new UserDTO("123", "username", "123", "123", "lastName", UserRole.USER);

        assertEquals(rightUserDTO, userDTO);
    }

}
