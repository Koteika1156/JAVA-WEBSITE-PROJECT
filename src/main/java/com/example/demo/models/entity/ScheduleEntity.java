package com.example.demo.models.entity;

import com.example.demo.models.Prototype;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "schedules")
@NoArgsConstructor
public class ScheduleEntity implements Prototype<ScheduleEntity> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private DoctorEntity doctor;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "clinic_id")
    private ClinicEntity clinic;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private ScheduleEntity(ScheduleEntity scheduleEntity) {
        this.id = scheduleEntity.getId();
        this.doctor = scheduleEntity.getDoctor();
        this.user = scheduleEntity.getUser();
        this.clinic = scheduleEntity.getClinic();
        this.startTime = scheduleEntity.getStartTime();
        this.endTime = scheduleEntity.getEndTime();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleEntity that = (ScheduleEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(doctor, that.doctor)
                && Objects.equals(user, that.user) && Objects.equals(clinic, that.clinic)
                && Objects.equals(startTime, that.startTime) && Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, doctor, user, clinic, startTime, endTime);
    }


    @Override
    public ScheduleEntity clone() {
        return new ScheduleEntity(this);
    }
}
