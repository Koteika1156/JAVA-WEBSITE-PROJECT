package com.example.demo.models.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class TokenRefreshResponse {
    private String accessToken;
}
