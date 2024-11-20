package com.example.demo.models.response.clinic;

import com.example.demo.models.entity.ClinicEntity;
import com.example.demo.models.interfaces.ModelWithMessage;
import com.example.demo.models.response.doctor.DoctorResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;
import java.util.List;

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

    private List<DoctorResponse> doctors;

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
                .doctors(
                        clinic.getDoctors().stream()
                                .map(DoctorResponse::toDoctorResponse)
                                .toList()
                )
                .build();
    }
}
