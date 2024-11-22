package com.example.demo.models.response.user;

import com.example.demo.models.UserRole;
import com.example.demo.models.entity.UserEntity;
import com.example.demo.models.interfaces.ModelWithMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Getter
@Setter
@SuperBuilder
public class UserResponse extends ModelWithMessage {
    private String id;
    private String firstName;
    private String lastName;
    private String number;
    private UserRole role;

    public static UserResponse toResponse(UserEntity user) {
        if (user == null) {
            return null;
        }

        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .number(user.getNumber())
                .role(user.getRole())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserResponse that = (UserResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(number, that.number) && role == that.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, number, role);
    }
}
