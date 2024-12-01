package com.example.demo.models.request.user;


import com.example.demo.models.Prototype;
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
public class UserRegistrationRequest implements Prototype<UserRegistrationRequest> {
    private String firstName;
    private String lastName;
    private String number;

    private String username;
    private String password;
    private String confirmPassword;
    private UserRole role;

    public UserRegistrationRequest(UserRegistrationRequest userRegistrationRequest) {
        this.firstName = userRegistrationRequest.getFirstName();
        this.lastName = userRegistrationRequest.getLastName();
        this.number = userRegistrationRequest.getNumber();
        this.username = userRegistrationRequest.getUsername();
        this.password = userRegistrationRequest.getPassword();
        this.confirmPassword = userRegistrationRequest.getConfirmPassword();
        this.role = userRegistrationRequest.getRole();
    }

    public static UserEntity toEntity(UserRegistrationRequest userRegistrationRequest) {
        if (userRegistrationRequest == null) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setFirstName(userRegistrationRequest.getFirstName());
        userEntity.setLastName(userRegistrationRequest.getLastName());
        userEntity.setNumber(userRegistrationRequest.getNumber());
        userEntity.setUsername(userRegistrationRequest.getUsername());
        userEntity.setPassword(userRegistrationRequest.getPassword());

        // Блочим челов которые хотят себе роль админа
        if (userRegistrationRequest.role == UserRole.ADMIN) {
            userEntity.setRole(UserRole.USER);
        }
        userEntity.setRole(userRegistrationRequest.getRole());

        return userEntity;
    }

    @Override
    public UserRegistrationRequest clone() {
        return new UserRegistrationRequest(this);
    }
}
