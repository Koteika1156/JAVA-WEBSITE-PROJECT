package com.example.demo.models.response.user;

import com.example.demo.models.interfaces.ResultWithMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class UserDeleteResponse extends ResultWithMessage {
    private boolean result;
    private String message;
}
