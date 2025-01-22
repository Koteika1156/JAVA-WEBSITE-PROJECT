package com.example.demo.models.dto;

import com.example.demo.models.Prototype;
import com.example.demo.models.entity.DoctorEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO implements Prototype<DoctorDTO> {

    private String id;
    private String firstName;
    private String lastName;
    private String specialization;


    public DoctorDTO(DoctorDTO doctorDTO) {
        this.id = doctorDTO.getId();
        this.firstName = doctorDTO.getFirstName();
        this.lastName = doctorDTO.getLastName();
        this.specialization = doctorDTO.getSpecialization();
    }

    public static DoctorDTO toDTO(DoctorEntity doctorEntity) {
        if (doctorEntity == null) {
            return null;
        }

        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setId(doctorEntity.getId());
        doctorDTO.setFirstName(doctorEntity.getFirstName());
        doctorDTO.setLastName(doctorEntity.getLastName());
        doctorDTO.setSpecialization(doctorEntity.getSpecialization());

        return doctorDTO;
    }

    @Override
    public DoctorDTO clone() {
        return new DoctorDTO(this);
    }
}
