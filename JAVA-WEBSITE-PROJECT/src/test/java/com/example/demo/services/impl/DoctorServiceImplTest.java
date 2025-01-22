package com.example.demo.services.impl;

import com.example.demo.models.entity.ClinicEntity;
import com.example.demo.models.entity.DoctorEntity;
import com.example.demo.models.request.doctor.DoctorRequest;
import com.example.demo.models.response.doctor.DoctorAddResponse;
import com.example.demo.models.response.doctor.DoctorDeleteResponse;
import com.example.demo.models.response.doctor.DoctorResponse;
import com.example.demo.models.response.doctor.DoctorsResponse;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.services.ClinicService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class DoctorServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private ClinicService clinicService;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    final int okStatusCode = 200;
    final int badStatusCode = 400;

    @Test
    void testGetDoctor() {
        String doctorId = "123";
        DoctorEntity doctorEntity = new DoctorEntity();

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctorEntity));

        ResponseEntity<DoctorResponse> response = doctorService.getDoctor(doctorId);

        assertEquals(okStatusCode, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetDoctor_NotFound() {
        String doctorId = "123";

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());

        ResponseEntity<DoctorResponse> response = doctorService.getDoctor(doctorId);

        assertEquals(badStatusCode, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getMessage());
    }

    @Test
    void testGetAllDoctors() {
        List<DoctorEntity> doctorEntities = new ArrayList<DoctorEntity>();

        doctorEntities.add(new DoctorEntity());
        doctorEntities.add(new DoctorEntity());

        when(doctorRepository.findAll()).thenReturn(doctorEntities);

        ResponseEntity<DoctorsResponse> response = doctorService.getAll();

        assertEquals(okStatusCode, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(doctorEntities.size(), response.getBody().getDoctors().size());
    }

    @Test
    void testGetAllDoctors_EmptyList() {
        when(doctorRepository.findAll()).thenReturn(new ArrayList<DoctorEntity>());

        ResponseEntity<DoctorsResponse> response = doctorService.getAll();

        assertEquals(okStatusCode, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().getDoctors().size());
    }

    @Test
    void testAddDoctor() {
        String clinicId = "123";
        DoctorRequest request = new DoctorRequest();
        request.setClinicId(clinicId);
        DoctorEntity doctorEntity = new DoctorEntity();

        when(doctorRepository.save(ArgumentMatchers.any(DoctorEntity.class))).thenReturn(doctorEntity);
        when(clinicService.getClinicById(clinicId)).thenReturn(Optional.of(new ClinicEntity()));
        ResponseEntity<DoctorAddResponse> response = doctorService.addDoctor(request);

        assertEquals(okStatusCode, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isResult());
        assertNotNull(response.getBody().getMessage());
    }

    @Test
    void testAddDoctor_Failure() {
        DoctorRequest request = new DoctorRequest();

        when(doctorRepository.save(ArgumentMatchers.any(DoctorEntity.class))).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<DoctorAddResponse> response = doctorService.addDoctor(request);

        assertEquals(badStatusCode, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isResult());
        assertEquals("Ошибка при добавлении доктора!", response.getBody().getMessage());
    }

    @Test
    void testDeleteDoctor() {
        String doctorId = "123";

        doNothing().when(doctorRepository).deleteById(doctorId);

        ResponseEntity<DoctorDeleteResponse> response = doctorService.deleteDoctor(doctorId);

        assertEquals(okStatusCode, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isResult());
        assertEquals("Успешно!", response.getBody().getMessage());
    }

    @Test
    void testDeleteDoctor_Failure() {
        String doctorId = "123";

        doThrow(new RuntimeException("Deletion error")).when(doctorRepository).deleteById(doctorId);

        ResponseEntity<DoctorDeleteResponse> response = doctorService.deleteDoctor(doctorId);

        assertNotNull(response.getBody());
        assertEquals(badStatusCode, response.getStatusCode().value());
        assertFalse(response.getBody().isResult());
        assertEquals("Ошибка при удалении доктора", response.getBody().getMessage());
    }
}
