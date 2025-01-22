package org.example.authservice.models.response.user;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.authservice.models.interfaces.TokenCredentials;

@Getter
@Setter
@SuperBuilder
public class UserRegistrationResponse extends TokenCredentials {
    private boolean result;
    private String message;
}
