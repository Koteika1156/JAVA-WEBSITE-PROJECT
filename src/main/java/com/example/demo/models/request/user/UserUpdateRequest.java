package com.example.demo.models.request.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    private String newPassword;
    private String newPasswordConfirm;
    private String firstName;
    private String lastName;
    private String number;
}
