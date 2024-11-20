package com.example.demo.models.response.user;

import com.example.demo.models.interfaces.TokenCredentials;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class UserRegistrationResponse extends TokenCredentials {
    private boolean result;
    private String message;
}
