package com.example.demo.models.request.doctor;

import com.example.demo.exception.ClinicNotFound;
import com.example.demo.models.entity.ClinicEntity;
import com.example.demo.models.entity.DoctorEntity;
import com.example.demo.services.ClinicService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorRequest {
    private String firstName;
    private String lastName;

    private String specialization;
    private String clinicId;

    public static DoctorEntity toEntity(DoctorRequest doctorRequest, ClinicService clinicService) {
        if (doctorRequest == null) {
            return null;
        }

        DoctorEntity doctorEntity = new DoctorEntity();
        doctorEntity.setFirstName(doctorRequest.getFirstName());
        doctorEntity.setLastName(doctorRequest.getLastName());
        doctorEntity.setSpecialization(doctorRequest.getSpecialization());

        ClinicEntity clinic = clinicService.getClinicById(doctorRequest.getClinicId()).orElseThrow(
                () -> new ClinicNotFound("Такая клиника не найдена!")
        );

        doctorEntity.setClinic(clinic);

        return doctorEntity;
    }
}
