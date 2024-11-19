package com.example.demo.models.request;


import com.example.demo.models.Roles;
import com.example.demo.models.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {
    private String firstName;
    private String lastName;
    private String number;

    private String username;
    private String password;
    private String confirmPassword;
    private Roles role;

    public static UserEntity toEntity(UserRegistrationRequest userRegistrationRequest) {
        UserEntity userEntity = new UserEntity();

        userEntity.setFirstName(userRegistrationRequest.getFirstName());
        userEntity.setLastName(userRegistrationRequest.getLastName());
        userEntity.setNumber(userRegistrationRequest.getNumber());
        userEntity.setUsername(userRegistrationRequest.getUsername());
        userEntity.setPassword(userRegistrationRequest.getPassword());

        // Блочим челов которые хотят себе роль админа
        if (userRegistrationRequest.role == Roles.ADMIN) {
            userEntity.setRole(Roles.USER);
        }
        userEntity.setRole(userRegistrationRequest.getRole());

        return userEntity;
    }
}
