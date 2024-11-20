package com.example.demo.models.interfaces;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public abstract class ResultWithMessage {
    private String message;
    private boolean result;
}
