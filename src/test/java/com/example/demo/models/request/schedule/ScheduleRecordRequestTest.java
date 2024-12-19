package com.example.demo.models.request.schedule;

import com.example.demo.models.entity.DoctorEntity;
import com.example.demo.models.entity.ScheduleEntity;
import com.example.demo.models.entity.UserEntity;
import com.example.demo.services.DoctorService;
import com.example.demo.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ScheduleRecordRequestTest {

    @Mock
    private DoctorService doctorService;

    @Mock
    private UserService userService;

    @Test
    void toEntity_justWorks() {
        String doctorId = "123";
        String userId = "234";
        String clinicId = "345";
        LocalDateTime startTime = LocalDateTime.of(2020, 1, 1, 1, 1);
        LocalDateTime endTime = LocalDateTime.of(2020, 1, 1, 1, 5);

        UserEntity userEntity = new UserEntity();
        DoctorEntity doctorEntity = new DoctorEntity();

        ScheduleRecordRequest scheduleRecordRequest = new ScheduleRecordRequest(
                doctorId, userId, clinicId, startTime, endTime
        );

        when(userService.getUserEntityById(userId)).thenReturn(Optional.of(userEntity));
        when(doctorService.getDoctorById(doctorId)).thenReturn(Optional.of(doctorEntity));

        ScheduleEntity scheduleEntity = ScheduleRecordRequest.toEntity(scheduleRecordRequest, doctorService, userService);

        ScheduleEntity rightScheduleEntity = new ScheduleEntity();

        rightScheduleEntity.setStartTime(startTime);
        rightScheduleEntity.setEndTime(endTime);
        rightScheduleEntity.setUser(userEntity);
        rightScheduleEntity.setDoctor(doctorEntity);

        assertNotNull(scheduleEntity);
        assertEquals(rightScheduleEntity, scheduleEntity);
    }

    @Test
    void toEntity_withNullRequest() {
        ScheduleRecordRequest scheduleRecordRequest = null;

        assertDoesNotThrow(() -> ScheduleRecordRequest.toEntity(scheduleRecordRequest, doctorService, userService));

        ScheduleEntity scheduleEntity = ScheduleRecordRequest.toEntity(scheduleRecordRequest, doctorService, userService);

        assertEquals(null, scheduleEntity);
    }
}
