package com.example.demo.models.response.clinic;

import com.example.demo.models.entity.ClinicEntity;
import com.example.demo.models.interfaces.ModelWithMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;

@Getter
@Setter
@SuperBuilder
public class ClinicResponse extends ModelWithMessage {
    private String id;

    private String name;
    private String address;
    private String phone;
    private String email;

    private LocalTime openTime;
    private LocalTime closeTime;

    public static ClinicResponse toResponse(ClinicEntity clinic) {
        if (clinic == null) {
            return null;
        }

        return ClinicResponse
                .builder()
                .id(clinic.getId())
                .name(clinic.getName())
                .address(clinic.getAddress())
                .phone(clinic.getPhone())
                .email(clinic.getEmail())
                .openTime(clinic.getOpenTime())
                .closeTime(clinic.getCloseTime())
                .build();
    }
}
