package com.example.demo.models.response.user;

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
public class UserResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String number;
    private UserRole role;

    public static UserResponse toResponse(UserEntity user) {
        if (user == null) {
            return null;
        }

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setNumber(user.getNumber());
        response.setRole(user.getRole());

        return response;
    }
}
