package com.example.demo.models.dto;

import com.example.demo.models.UserRole;
import com.example.demo.models.entity.UserEntity;
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
        userEntity.setPassword("lastName");
        userEntity.setRole(UserRole.USER);

        UserDTO userDTO = UserDTO.toDTO(userEntity);

        UserDTO rightUserDTO = new UserDTO("123", "username", "lastName", UserRole.USER);

        assertEquals(rightUserDTO, userDTO);
    }

    @Test
    public void toDTO_withNullEntity() {
        UserEntity userEntity = null;


        assertDoesNotThrow(() -> UserDTO.toDTO(userEntity));

        UserDTO userDTO = UserDTO.toDTO(userEntity);

        assertNull(userDTO);
    }

}
