package org.example.authservice.models.response.user;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.authservice.models.interfaces.ModelWithMessage;
import org.example.dtolib.dto.UserRole;

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
