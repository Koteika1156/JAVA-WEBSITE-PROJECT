package com.example.demo.services.impl;

import com.example.demo.models.entity.ClinicEntity;
import com.example.demo.models.request.clinic.ClinicAddRequest;
import com.example.demo.models.response.clinic.ClinicAddResponse;
import com.example.demo.models.response.clinic.ClinicDeleteResponse;
import com.example.demo.models.response.clinic.ClinicResponse;
import com.example.demo.models.response.clinic.ClinicsResponse;
import com.example.demo.repository.ClinicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClinicServiceImplTest {

    @Mock
    private ClinicRepository clinicRepository;

    @InjectMocks
    private ClinicServiceImpl clinicService;

    final int okStatusCode = 200;
    final int badStatusCode = 400;

    @BeforeEach
    void setUp() {
        // any setup code if required
    }

    @Test
    void testGetClinic() {
        String clinicId = "123";
        ClinicEntity clinicEntity = new ClinicEntity();

        when(clinicRepository.findById(clinicId)).thenReturn(Optional.of(clinicEntity));

        ResponseEntity<ClinicResponse> response = clinicService.getClinic(clinicId);

        assertNotNull(response.getBody());
        assertEquals(okStatusCode, response.getStatusCode().value());
    }

    @Test
    void testGetClinic_NotFound() {
        String clinicId = "123";

        when(clinicRepository.findById(clinicId)).thenReturn(Optional.empty());

        ResponseEntity<ClinicResponse> response = clinicService.getClinic(clinicId);

        assertEquals(badStatusCode, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getMessage());
    }

    @Test
    void testGetClinics() {
        List<ClinicEntity> clinicEntities = List.of(new ClinicEntity(), new ClinicEntity());

        when(clinicRepository.findAll()).thenReturn(clinicEntities);

        ResponseEntity<ClinicsResponse> response = clinicService.getClinics();

        assertNotNull(response.getBody());
        assertEquals(okStatusCode, response.getStatusCode().value());
        assertEquals(clinicEntities.size(), response.getBody().getClinics().size());
        assertNotNull(response.getBody().getMessage());
    }

    @Test
    void testGetClinics_EmptyList() {
        when(clinicRepository.findAll()).thenReturn(List.of());

        ResponseEntity<ClinicsResponse> response = clinicService.getClinics();

        assertNotNull(response.getBody());
        assertEquals(okStatusCode, response.getStatusCode().value());
        assertEquals(0, response.getBody().getClinics().size());
        assertNotNull(response.getBody().getMessage());
    }

    @Test
    void testAddClinic() {
        ClinicAddRequest request = new ClinicAddRequest();
        ClinicEntity clinicEntity = new ClinicEntity();

        when(clinicRepository.save(ArgumentMatchers.any(ClinicEntity.class))).thenReturn(clinicEntity);

        ResponseEntity<ClinicAddResponse> response = clinicService.addClinic(request);

        assertNotNull(response.getBody());
        assertEquals(okStatusCode, response.getStatusCode().value());
        assertTrue(response.getBody().isResult());
        assertEquals("Успешно!", response.getBody().getMessage());
    }

    @Test
    void testAddClinic_Failure() {
        ClinicAddRequest request = new ClinicAddRequest();

        when(clinicRepository.save(ArgumentMatchers.any(ClinicEntity.class))).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<ClinicAddResponse> response = clinicService.addClinic(request);

        assertNotNull(response.getBody());
        assertEquals(badStatusCode, response.getStatusCode().value());
        assertFalse(response.getBody().isResult());
        assertNotNull(response.getBody().getMessage());
    }

    @Test
    void testDeleteClinic() {
        String clinicId = "123";

        doNothing().when(clinicRepository).deleteById(clinicId);

        ResponseEntity<ClinicDeleteResponse> response = clinicService.deleteClinic(clinicId);

        assertNotNull(response.getBody());
        assertEquals(okStatusCode, response.getStatusCode().value());
        assertTrue(response.getBody().isResult());
        assertNotNull(response.getBody().getMessage());
    }

    @Test
    void testDeleteClinic_Failure() {
        String clinicId = "123";

        doThrow(new RuntimeException("Deletion error")).when(clinicRepository).deleteById(clinicId);

        ResponseEntity<ClinicDeleteResponse> response = clinicService.deleteClinic(clinicId);

        assertNotNull(response.getBody());
        assertEquals(badStatusCode, response.getStatusCode().value());
        assertFalse(response.getBody().isResult());
        assertNotNull(response.getBody().getMessage());
    }
}
