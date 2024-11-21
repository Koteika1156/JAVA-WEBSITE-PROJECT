package com.example.demo.controllers;

import com.example.demo.models.request.doctor.DoctorRequest;
import com.example.demo.models.response.doctor.DoctorAddResponse;
import com.example.demo.models.response.doctor.DoctorDeleteResponse;
import com.example.demo.models.response.doctor.DoctorResponse;
import com.example.demo.models.response.doctor.DoctorsResponse;
import com.example.demo.services.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/doctor")
public class DoctorController {
    DoctorService doctorService;

    DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/getDoctor")
    public ResponseEntity<DoctorResponse> getDoctor(@RequestParam String id) {
        return doctorService.getDoctor(id);
    }

    @GetMapping("/all")
    public ResponseEntity<DoctorsResponse> getDoctor() {
        return doctorService.getAll();
    }

    @PostMapping("/add")
    public ResponseEntity<DoctorAddResponse> addDoctor(@RequestBody DoctorRequest doctorRequest) {
        return doctorService.addDoctor(doctorRequest);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<DoctorDeleteResponse> deleteDoctor(@RequestParam String id) {
        return doctorService.deleteDoctor(id);
    }
}
