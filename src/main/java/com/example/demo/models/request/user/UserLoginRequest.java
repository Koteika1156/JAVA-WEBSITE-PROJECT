package com.example.demo.models.request.user;

import com.example.demo.models.Prototype;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
