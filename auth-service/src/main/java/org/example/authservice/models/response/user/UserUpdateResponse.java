package org.example.authservice.models.response.user;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.authservice.models.interfaces.ResultWithMessage;

@Getter
@Setter
@SuperBuilder
public class UserUpdateResponse extends ResultWithMessage {
}
