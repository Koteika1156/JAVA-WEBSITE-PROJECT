package com.example.demo.services.impl;

import com.example.demo.models.entity.ClinicEntity;
import com.example.demo.models.entity.DoctorEntity;
import com.example.demo.models.entity.ScheduleEntity;
import com.example.demo.models.entity.UserEntity;
import com.example.demo.models.request.schedule.ScheduleRecordRequest;
import com.example.demo.models.request.schedule.ScheduleUpdateRequest;
import com.example.demo.models.response.schedule.*;
import com.example.demo.repository.ScheduleRepository;
import com.example.demo.services.ClinicService;
import com.example.demo.services.DoctorService;
import com.example.demo.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ScheduleServiceImplTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private ClinicService clinicService;

    @Mock
    private DoctorService doctorService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ScheduleServiceImpl scheduleService;

    final int okStatusCode = 200;
    final int badStatusCode = 400;

    @Test
    void addRecord_ValidRequest_ReturnsSuccessResponse() {
        String userId = "123";

        // Arrange
        ScheduleRecordRequest request = new ScheduleRecordRequest();
        request.setUserId(userId);
        request.setDoctorId("doctor123");
        request.setStartTime(LocalDate.of(2024, 11, 23).atTime(10, 0));
        request.setEndTime(LocalDate.of(2024, 11, 23).atTime(11, 0));

        DoctorEntity doctor = new DoctorEntity();
        doctor.setId("doctor123");
        ClinicEntity clinic = new ClinicEntity();
        clinic.setId("clinic456");
        clinic.setOpenTime(LocalTime.of(8, 0));
        clinic.setCloseTime(LocalTime.of(18, 0));
        doctor.setClinic(clinic);
        when(userService.getUserEntityById(userId)).thenReturn(Optional.of(new UserEntity()));
        when(doctorService.getDoctorById("doctor123")).thenReturn(Optional.of(doctor));
        when(clinicService.getClinicById("clinic456")).thenReturn(Optional.of(clinic));
        when(scheduleRepository.findByStartTimeAndDoctorId(any(), any())).thenReturn(Optional.empty());
        when(scheduleRepository.existsByDoctorIdAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(any(), any(), any())).thenReturn(false);

        // Act
        ResponseEntity<ScheduleRecordResponse> response = scheduleService.addRecord(request);

        // Assert
        assertNotNull(response.getBody());
        assertEquals(okStatusCode, response.getStatusCode().value());
        assertTrue(response.getBody().isResult());
        assertNotNull(response.getBody().getMessage());
        verify(scheduleRepository, times(1)).save(any(ScheduleEntity.class));
    }

    @Test
    void addRecord_TimeOutsideClinicHours_ThrowsException() {
        // Arrange
        ScheduleRecordRequest request = new ScheduleRecordRequest();
        request.setDoctorId("doctor123");
        request.setStartTime(LocalDate.of(2024, 11, 23).atTime(19, 0));
        request.setEndTime(LocalDate.of(2024, 11, 23).atTime(20, 0));

        DoctorEntity doctor = new DoctorEntity();
        doctor.setId("doctor123");
        ClinicEntity clinic = new ClinicEntity();
        clinic.setId("clinic456");
        clinic.setOpenTime(LocalTime.of(8, 0));
        clinic.setCloseTime(LocalTime.of(18, 0));
        doctor.setClinic(clinic);

        when(doctorService.getDoctorById("doctor123")).thenReturn(Optional.of(doctor));
        when(clinicService.getClinicById("clinic456")).thenReturn(Optional.of(clinic));

        // Act
        ResponseEntity<ScheduleRecordResponse> response = scheduleService.addRecord(request);

        // Assert
        assertNotNull(response.getBody());
        assertEquals(badStatusCode, response.getStatusCode().value());
        assertFalse(response.getBody().isResult());
        assertNotNull(response.getBody().getMessage());
        verify(scheduleRepository, never()).save(any());
    }

    @Test
    void updateTime_ValidRequest_ReturnsSuccessResponse() {
        // Arrange
        ScheduleUpdateRequest request = new ScheduleUpdateRequest();
        request.setId("schedule123");
        request.setNewStartTime(LocalDate.of(2024, 11, 23).atTime(10, 0));
        request.setNewEndTime(LocalDate.of(2024, 11, 23).atTime(11, 0));

        ScheduleEntity schedule = new ScheduleEntity();
        schedule.setId("schedule123");
        DoctorEntity doctor = new DoctorEntity();
        doctor.setId("doctor123");
        ClinicEntity clinic = new ClinicEntity();
        clinic.setOpenTime(LocalTime.of(8, 0));
        clinic.setCloseTime(LocalTime.of(18, 0));
        schedule.setDoctor(doctor);

        when(scheduleRepository.findById("schedule123")).thenReturn(Optional.of(schedule));
        when(clinicService.getClinicById(any())).thenReturn(Optional.of(clinic));
        when(scheduleRepository.existsByDoctorIdAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(any(), any(), any())).thenReturn(false);

        // Act
        ResponseEntity<ScheduleUpdateResponse> response = scheduleService.updateTime(request);

        // Assert
        assertNotNull(response.getBody());
        assertEquals(okStatusCode, response.getStatusCode().value());
        assertTrue(response.getBody().isResult());
        assertNotNull(response.getBody().getMessage());
        verify(scheduleRepository, times(1)).save(schedule);
    }

    @Test
    void deleteRecord_ValidId_ReturnsSuccessResponse() {
        // Arrange
        String recordId = "schedule123";

        doNothing().when(scheduleRepository).deleteById(recordId);

        // Act
        ResponseEntity<ScheduleDeleteResponse> response = scheduleService.deleteRecord(recordId);

        // Assert
        assertNotNull(response.getBody());
        assertEquals(okStatusCode, response.getStatusCode().value());
        assertTrue(response.getBody().isResult());
        assertNotNull(response.getBody().getMessage());
        verify(scheduleRepository, times(1)).deleteById(recordId);
    }

    @Test
    void getRecords_ReturnsListOfSchedules() {
        // Arrange
        ScheduleEntity schedule = new ScheduleEntity();
        schedule.setId("schedule123");
        when(scheduleRepository.findAll()).thenReturn(List.of(schedule));

        // Act
        ResponseEntity<SchedulesResponse> response = scheduleService.getRecords();

        // Assert
        assertNotNull(response.getBody());
        assertEquals(okStatusCode, response.getStatusCode().value());
        assertEquals(1, response.getBody().getSchedules().size());
        assertNotNull(response.getBody().getMessage());
    }

    @Test
    void getRecord_ValidId_ReturnsSchedule() {
        // Arrange
        String recordId = "schedule123";
        ScheduleEntity schedule = new ScheduleEntity();
        schedule.setId(recordId);
        when(scheduleRepository.findById(recordId)).thenReturn(Optional.of(schedule));

        // Act
        ResponseEntity<ScheduleResponse> response = scheduleService.getRecord(recordId);

        // Assert
        assertEquals(okStatusCode, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(recordId, response.getBody().getId());
    }

    @Test
    void getRecord_InvalidId_ReturnsErrorResponse() {
        // Arrange
        String recordId = "invalidId";
        when(scheduleRepository.findById(recordId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ScheduleResponse> response = scheduleService.getRecord(recordId);

        // Assert
        assertEquals(badStatusCode, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getMessage());
        assertNull(response.getBody().getId());
    }
}
