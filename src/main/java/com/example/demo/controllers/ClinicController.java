package com.example.demo.controllers;

import com.example.demo.models.request.clinic.ClinicAddRequest;
import com.example.demo.models.response.clinic.ClinicAddResponse;
import com.example.demo.models.response.clinic.ClinicDeleteResponse;
import com.example.demo.models.response.clinic.ClinicResponse;
import com.example.demo.models.response.clinic.ClinicsResponse;
import com.example.demo.services.ClinicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clinic")
public class ClinicController {

    private final ClinicService clinicService;

    public ClinicController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @GetMapping("/getClinic")
    public ResponseEntity<ClinicResponse> getClinic(@RequestParam String clinicId) {
        return clinicService.getClinic(clinicId);
    }

    @GetMapping("/all")
    public ResponseEntity<ClinicsResponse> getClinics() {
        return clinicService.getClinics();
    }

    @PostMapping("/add")
    public ResponseEntity<ClinicAddResponse> addClinic(@RequestBody ClinicAddRequest clinicAddRequest) {
        return clinicService.addClinic(clinicAddRequest);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ClinicDeleteResponse> deleteClinic(@RequestParam String clinicId) {
        return clinicService.deleteClinic(clinicId);
    }
}
