package com.example.demo.models.entity;

import com.example.demo.models.Prototype;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "doctors")
@NoArgsConstructor
public class DoctorEntity implements Prototype<DoctorEntity> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String firstName;
    private String lastName;

    private String specialization;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "clinic_id")
    private ClinicEntity clinic;

    private DoctorEntity(DoctorEntity doctorEntity) {
        this.id = doctorEntity.getId();
        this.firstName = doctorEntity.getFirstName();
        this.lastName = doctorEntity.getLastName();
        this.specialization = doctorEntity.getSpecialization();
        this.clinic = doctorEntity.getClinic();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoctorEntity that = (DoctorEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(specialization, that.specialization) && Objects.equals(clinic, that.clinic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, specialization, clinic);
    }

    @Override
    public DoctorEntity clone() {
        return new DoctorEntity(this);
    }
}
