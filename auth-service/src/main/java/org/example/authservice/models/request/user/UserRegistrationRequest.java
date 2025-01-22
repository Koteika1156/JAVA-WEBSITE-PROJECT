package org.example.authservice.models.request.user;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.authservice.models.Prototype;
import org.example.dtolib.dto.UserRole;

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

    @Override
    public UserRegistrationRequest clone() {
        return new UserRegistrationRequest(this);
    }
}
