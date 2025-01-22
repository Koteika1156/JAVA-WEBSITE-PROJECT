package com.example.demo.models.request.doctor;

import com.example.demo.models.entity.ClinicEntity;
import com.example.demo.models.entity.DoctorEntity;
import com.example.demo.services.ClinicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class DoctorRequestTest {

    @Mock
    private ClinicService clinicService;

    private final String clinicID = "clinicId";
    private final ClinicEntity clinicEntity = new ClinicEntity();

    @BeforeEach
    public void setUp() {
        when(clinicService.getClinicById(clinicID)).thenReturn(Optional.of(clinicEntity));
    }

    @Test
    public void toEntity_justWorks() {
        String firstName = "firstName";
        String lastName = "lastName";
        String specialization = "specialization";
        String clinicId = "clinicId";

        DoctorRequest doctorRequest = new DoctorRequest(firstName, lastName, specialization, clinicId);
        DoctorEntity doctorEntity = DoctorRequest.toEntity(doctorRequest, clinicService);

        DoctorEntity rightDoctorEntity = new DoctorEntity();
        rightDoctorEntity.setFirstName(firstName);
        rightDoctorEntity.setLastName(lastName);
        rightDoctorEntity.setSpecialization(specialization);
        rightDoctorEntity.setClinic(clinicEntity);

        assertEquals(doctorEntity, rightDoctorEntity);
    }

    @Test
    public void toEntity_withNullRequest() {
        DoctorRequest doctorRequest = null;

        assertDoesNotThrow(() -> DoctorRequest.toEntity(doctorRequest, clinicService));

        DoctorEntity doctorEntity = DoctorRequest.toEntity(doctorRequest, clinicService);

        assertEquals(null, doctorEntity);
    }

}
