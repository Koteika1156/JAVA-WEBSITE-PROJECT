package com.example.demo.models.response;

import com.example.demo.models.interfaces.TokenCredentials;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class UserLoginResponse extends TokenCredentials {
    private boolean result;
}
