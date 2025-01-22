package com.example.demo.models.response.clinic;

import com.example.demo.models.entity.ClinicEntity;
import com.example.demo.models.entity.DoctorEntity;
import com.example.demo.models.response.doctor.DoctorResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
public class ClinicResponseTest {

    @Test
    public void toResponse_justWorks() {
        ClinicEntity clinic = new ClinicEntity();

        final String id = "1";
        final String name = "name";
        final String address = "address";
        final String phone = "phone";
        final String email = "email";

        List<DoctorEntity> doctors = List.of(new DoctorEntity());

        clinic.setId(id);
        clinic.setName(name);
        clinic.setAddress(address);
        clinic.setPhone(phone);
        clinic.setEmail(email);

        clinic.setOpenTime(LocalTime.MIDNIGHT);
        clinic.setCloseTime(LocalTime.NOON);

        clinic.setDoctors(doctors);

        ClinicResponse clinicResponse = ClinicResponse.toResponse(clinic);


        ClinicResponse rightClinicResponse = ClinicResponse.builder()
                .id(id)
                .name(name)
                .address(address)
                .phone(phone)
                .email(email)
                .openTime(LocalTime.MIDNIGHT)
                .closeTime(LocalTime.NOON)
                .doctors(
                        doctors
                                .stream()
                                .map(DoctorResponse::toResponse)
                                .toList()
                )
                .build();
        assertEquals(rightClinicResponse, clinicResponse);
    }

    @Test
    public void toResponse_withNullEntity() {
        ClinicEntity clinic = null;

        assertDoesNotThrow(() -> ClinicResponse.toResponse(clinic));

        ClinicResponse clinicResponse = ClinicResponse.toResponse(clinic);

        assertEquals(null, clinicResponse);
    }

    @Test
    public void toResponse_withNullDoctors() {
        ClinicEntity clinic = new ClinicEntity();

        final String id = "1";
        final String name = "name";
        final String address = "address";
        final String phone = "phone";
        final String email = "email";

        List<DoctorEntity> doctors = null;

        clinic.setId(id);
        clinic.setName(name);
        clinic.setAddress(address);
        clinic.setPhone(phone);
        clinic.setEmail(email);

        clinic.setOpenTime(LocalTime.MIDNIGHT);
        clinic.setCloseTime(LocalTime.NOON);

        clinic.setDoctors(doctors);

        ClinicResponse clinicResponse = ClinicResponse.toResponse(clinic);


        ClinicResponse rightClinicResponse = ClinicResponse.builder()
                .id(id)
                .name(name)
                .address(address)
                .phone(phone)
                .email(email)
                .openTime(LocalTime.MIDNIGHT)
                .closeTime(LocalTime.NOON)
                .doctors(
                        List.of()
                )
                .build();
        assertEquals(rightClinicResponse, clinicResponse);
    }
}
