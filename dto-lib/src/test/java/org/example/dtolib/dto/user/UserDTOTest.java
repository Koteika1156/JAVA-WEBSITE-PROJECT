package org.example.dtolib.dto.user;

import org.example.dtolib.dto.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class UserDTOTest {

    @Test
    public void toDTO_justWorks() {
        UserDTO userDTO = UserDTO
                .builder()
                .id("123")
                .username("username")
                .password("123")
                .firstName("123")
                .lastName("lastName")
                .role(UserRole.USER)
                .build();

        UserDTO rightUserDTO = new UserDTO("123", "username", "123", "123", "lastName","1", UserRole.USER);

        assertEquals(rightUserDTO, userDTO);
    }
}
