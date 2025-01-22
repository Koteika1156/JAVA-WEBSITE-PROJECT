package org.example.authservice.models.request.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.authservice.models.Prototype;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest implements Prototype<UserUpdateRequest> {
    private String newPassword;
    private String newPasswordConfirm;
    private String firstName;
    private String lastName;
    private String number;

    public UserUpdateRequest(UserUpdateRequest userUpdateRequest) {
        this.newPassword = userUpdateRequest.getNewPassword();
        this.newPasswordConfirm = userUpdateRequest.getNewPasswordConfirm();
        this.firstName = userUpdateRequest.getFirstName();
        this.lastName = userUpdateRequest.getLastName();
        this.number = userUpdateRequest.getNumber();
    }

    @Override
    public UserUpdateRequest clone() {
        return new UserUpdateRequest(this);
    }
}
