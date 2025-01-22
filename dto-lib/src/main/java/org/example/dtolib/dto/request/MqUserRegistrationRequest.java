package org.example.dtolib.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.dtolib.dto.user.UserDTO;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MqUserRegistrationRequest implements Serializable {
    private UserDTO value;
}
