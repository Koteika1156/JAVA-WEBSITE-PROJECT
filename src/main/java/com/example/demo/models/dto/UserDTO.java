package com.example.demo.models.dto;

import com.example.demo.models.UserRole;
import com.example.demo.models.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String id;

    private String username;
    private String password;
    private UserRole role;

    public static UserDTO toDTO(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(userEntity.getId());
        userDTO.setUsername(userEntity.getUsername());
        userDTO.setPassword(userEntity.getPassword());
        userDTO.setRole(userEntity.getRole());
        return userDTO;
    }
}
