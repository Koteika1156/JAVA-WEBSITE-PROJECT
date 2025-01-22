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
public class UserLoginRequest implements Prototype<UserLoginRequest> {
    private String username;
    private String password;

    public UserLoginRequest(UserLoginRequest userLoginRequest) {
        this.username = userLoginRequest.getUsername();
        this.password = userLoginRequest.getPassword();
    }

    @Override
    public UserLoginRequest clone() {
        return new UserLoginRequest(this);
    }
}
