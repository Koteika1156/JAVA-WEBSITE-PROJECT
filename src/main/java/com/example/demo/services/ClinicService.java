package com.example.demo.services;

import com.example.demo.models.request.clinic.ClinicAddRequest;
import com.example.demo.models.response.clinic.ClinicAddResponse;
import com.example.demo.models.response.clinic.ClinicDeleteResponse;
import com.example.demo.models.response.clinic.ClinicResponse;
import com.example.demo.models.response.clinic.ClinicsResponse;
import org.springframework.http.ResponseEntity;

public interface ClinicService {

    ResponseEntity<ClinicResponse> getClinic(String clinicId);

    ResponseEntity<ClinicsResponse> getClinics();

    ResponseEntity<ClinicAddResponse> addClinic(ClinicAddRequest clinicAddRequest);

    ResponseEntity<?> updateClinic();

    ResponseEntity<ClinicDeleteResponse> deleteClinic(String clinicId);
}
