package com.example.demo.models.response.doctor;

import com.example.demo.models.entity.DoctorEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
public class DoctorResponseTest {

    @Test
    public void toResponse_justWorks() {
        DoctorEntity doctorEntity = new DoctorEntity();

        String id = "123";
        String name = "Doctor";
        String secondName = "secondName";
        String specialty = "specialty";

        doctorEntity.setId(id);
        doctorEntity.setFirstName(name);
        doctorEntity.setLastName(secondName);
        doctorEntity.setSpecialization(specialty);

        DoctorResponse doctorResponse = DoctorResponse.toResponse(doctorEntity);

        DoctorResponse rightDoctorResponse = DoctorResponse.builder()
                .id(id)
                .firstName(name)
                .lastName(secondName)
                .specialization(specialty)
                .build();

        assertEquals(rightDoctorResponse, doctorResponse);
    }

    @Test
    public void toResponse_withNullEntity() {
        DoctorEntity doctorEntity = null;

        assertDoesNotThrow(() -> DoctorResponse.toResponse(doctorEntity));
        DoctorResponse doctorResponse = DoctorResponse.toResponse(doctorEntity);

        assertEquals(null, doctorResponse);
    }
}
