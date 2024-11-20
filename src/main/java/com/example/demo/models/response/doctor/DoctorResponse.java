package com.example.demo.models.response.doctor;

import com.example.demo.models.entity.DoctorEntity;
import com.example.demo.models.interfaces.ModelWithMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class DoctorResponse extends ModelWithMessage {
    private String id;

    private String firstName;
    private String lastName;

    private String specialization;

    public static DoctorResponse toDoctorResponse(DoctorEntity doctorEntity) {
        if (doctorEntity == null) {
            return null;
        }

        return DoctorResponse.builder()
                .id(doctorEntity.getId())
                .firstName(doctorEntity.getFirstName())
                .lastName(doctorEntity.getLastName())
                .specialization(doctorEntity.getSpecialization())
                .build();
    }
}
