package com.example.demo.services;

import com.example.demo.models.entity.DoctorEntity;
import com.example.demo.models.request.doctor.DoctorRequest;
import com.example.demo.models.response.doctor.DoctorAddResponse;
import com.example.demo.models.response.doctor.DoctorDeleteResponse;
import com.example.demo.models.response.doctor.DoctorResponse;
import com.example.demo.models.response.doctor.DoctorsResponse;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface DoctorService {
    ResponseEntity<DoctorResponse> getDoctor(String id);

    ResponseEntity<DoctorsResponse> getAll();

    ResponseEntity<DoctorAddResponse> addDoctor(DoctorRequest doctorRequest);

    ResponseEntity<DoctorDeleteResponse> deleteDoctor(String id);

    Optional<DoctorEntity> getDoctorById(String id);
}
