package com.example.demo.models.response.schedule;

import com.example.demo.models.entity.ClinicEntity;
import com.example.demo.models.entity.DoctorEntity;
import com.example.demo.models.entity.ScheduleEntity;
import com.example.demo.models.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class ScheduleResponseTest {

    @Test
    void toResponse_justWorks() {
        String id = "123";
        String doctorId = "234";
        String userId = "345";
        String hospitalId = "456";
        LocalDateTime startTime = LocalDateTime.of(2020, 1, 1, 1, 1);
        LocalDateTime endTime = LocalDateTime.of(2020, 1, 1, 1, 5);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        DoctorEntity doctorEntity = new DoctorEntity();
        UserEntity userEntity = new UserEntity();
        ClinicEntity clinicEntity = new ClinicEntity();

        scheduleEntity.setId(id);
        scheduleEntity.setStartTime(startTime);
        scheduleEntity.setEndTime(endTime);

        doctorEntity.setId(doctorId);
        scheduleEntity.setDoctor(doctorEntity);
        userEntity.setId(userId);
        scheduleEntity.setUser(userEntity);
        clinicEntity.setId(hospitalId);
        scheduleEntity.setClinic(clinicEntity);

        ScheduleResponse rightScheduleResponse = ScheduleResponse.builder()
                .id(id)
                .doctorId(doctorId)
                .userId(userId)
                .hospitalId(hospitalId)
                .startTime(startTime)
                .endTime(endTime)
                .build();

        ScheduleResponse scheduleResponse = ScheduleResponse.toResponse(scheduleEntity);

        assertNotNull(scheduleResponse);
        assertEquals(rightScheduleResponse, scheduleResponse);
    }

    @Test
    void toResponse_withNullEntity() {
        ScheduleEntity scheduleEntity = null;

        assertDoesNotThrow(() -> ScheduleResponse.toResponse(scheduleEntity));
        ScheduleResponse scheduleResponse = ScheduleResponse.toResponse(scheduleEntity);

        assertEquals(null, scheduleResponse);
    }

    @Test
    void toResponse_withNullInnerEntities() {
        String id = "123";
        String doctorId = "234";
        String userId = "345";
        String hospitalId = "456";

        LocalDateTime startTime = LocalDateTime.of(2020, 1, 1, 1, 1);
        LocalDateTime endTime = LocalDateTime.of(2020, 1, 1, 1, 5);

        ScheduleEntity scheduleEntity = new ScheduleEntity();

        scheduleEntity.setId(id);
        scheduleEntity.setStartTime(startTime);
        scheduleEntity.setEndTime(endTime);

        scheduleEntity.setDoctor(null);
        scheduleEntity.setUser(null);
        scheduleEntity.setClinic(null);

        assertDoesNotThrow(() -> ScheduleResponse.toResponse(scheduleEntity));
        ScheduleResponse scheduleResponse = ScheduleResponse.toResponse(scheduleEntity);

        ScheduleResponse rightScheduleResponse = ScheduleResponse.builder()
                .id(id)
                .doctorId(null)
                .userId(null)
                .hospitalId(null)
                .startTime(startTime)
                .endTime(endTime)
                .build();

        assertNotNull(scheduleResponse);
        assertEquals(rightScheduleResponse, scheduleResponse);
    }
}
