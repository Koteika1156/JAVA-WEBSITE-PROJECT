package com.example.demo.models.request.clinic;

import com.example.demo.models.entity.ClinicEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
public class ClinicAddRequestTest {

    @Test
    public void toEntity_justWork() {
        String name = "name";
        String address = "address";
        String phone = "phone";
        String email = "email";
        LocalTime openTime = LocalTime.MIDNIGHT;
        LocalTime closeTime = LocalTime.NOON;

        ClinicAddRequest clinicAddRequest = new ClinicAddRequest(name, address, phone, email, openTime, closeTime);

        ClinicEntity clinic = ClinicAddRequest.toEntity(clinicAddRequest);

        ClinicEntity rightClinicEntity = new ClinicEntity();
        rightClinicEntity.setName(name);
        rightClinicEntity.setAddress(address);
        rightClinicEntity.setPhone(phone);
        rightClinicEntity.setEmail(email);
        rightClinicEntity.setOpenTime(openTime);
        rightClinicEntity.setCloseTime(closeTime);

        assertEquals(rightClinicEntity, clinic);
    }

    @Test
    public void toEntity_withNullRequest() {
        ClinicAddRequest clinicAddRequest = null;

        assertDoesNotThrow(() -> ClinicAddRequest.toEntity(clinicAddRequest));
        ClinicEntity clinic = ClinicAddRequest.toEntity(clinicAddRequest);

        assertEquals(null, clinic);
    }
}
