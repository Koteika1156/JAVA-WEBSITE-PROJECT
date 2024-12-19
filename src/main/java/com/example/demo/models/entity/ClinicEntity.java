package com.example.demo.models.entity;

import com.example.demo.models.Prototype;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "clinics")
@NoArgsConstructor
public class ClinicEntity implements Prototype<ClinicEntity> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String address;
    private String phone;
    private String email;

    private LocalTime openTime;
    private LocalTime closeTime;

    @OneToMany(mappedBy = "clinic")
    private List<DoctorEntity> doctors;

    private ClinicEntity(ClinicEntity clinicEntity) {
        this.id = clinicEntity.getId();
        this.name = clinicEntity.getName();
        this.address = clinicEntity.getAddress();
        this.phone = clinicEntity.getPhone();
        this.email = clinicEntity.getEmail();
        this.openTime = clinicEntity.getOpenTime();
        this.closeTime = clinicEntity.getCloseTime();
        this.doctors = clinicEntity.getDoctors();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClinicEntity clinic = (ClinicEntity) o;
        return Objects.equals(id, clinic.id) && Objects.equals(name, clinic.name) && Objects.equals(address, clinic.address) && Objects.equals(phone, clinic.phone) && Objects.equals(email, clinic.email) && Objects.equals(openTime, clinic.openTime) && Objects.equals(closeTime, clinic.closeTime) && Objects.equals(doctors, clinic.doctors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, phone, email, openTime, closeTime, doctors);
    }


    @Override
    public ClinicEntity clone() {
        return new ClinicEntity(this);
    }
}
