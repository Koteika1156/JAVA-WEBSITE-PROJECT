package org.example.authservice.models.interfaces;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public abstract class ModelWithMessage {
    private String message;
}
