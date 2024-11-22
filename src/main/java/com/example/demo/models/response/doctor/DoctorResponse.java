package com.example.demo.models.response.doctor;

import com.example.demo.models.entity.DoctorEntity;
import com.example.demo.models.interfaces.ModelWithMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Getter
@Setter
@SuperBuilder
public class DoctorResponse extends ModelWithMessage {
    private String id;

    private String firstName;
    private String lastName;

    private String specialization;

    public static DoctorResponse toResponse(DoctorEntity doctorEntity) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoctorResponse that = (DoctorResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(specialization, that.specialization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, specialization);
    }
}
