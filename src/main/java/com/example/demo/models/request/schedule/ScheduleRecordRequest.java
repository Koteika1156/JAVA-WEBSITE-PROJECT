package com.example.demo.models.request.schedule;

import com.example.demo.exception.ClinicNotFound;
import com.example.demo.exception.DoctorNotFound;
import com.example.demo.models.Prototype;
import com.example.demo.models.entity.DoctorEntity;
import com.example.demo.models.entity.ScheduleEntity;
import com.example.demo.models.entity.UserEntity;
import com.example.demo.services.DoctorService;
import com.example.demo.services.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleRecordRequest implements Prototype<ScheduleRecordRequest> {
    private String doctorId;
    private String userId;
    private String clinicId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public ScheduleRecordRequest(ScheduleRecordRequest scheduleRecordRequest) {
        this.doctorId = scheduleRecordRequest.getDoctorId();
        this.userId = scheduleRecordRequest.getUserId();
        this.clinicId = scheduleRecordRequest.getClinicId();
        this.startTime = scheduleRecordRequest.getStartTime();
        this.endTime = scheduleRecordRequest.getEndTime();
    }

    public static ScheduleEntity toEntity(ScheduleRecordRequest scheduleRequest, DoctorService doctorService, UserService userService) {
        if (scheduleRequest == null) {
            return null;
        }

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setStartTime(scheduleRequest.getStartTime());
        scheduleEntity.setEndTime(scheduleRequest.getEndTime());

        UserEntity user = userService.getUserEntityById(scheduleRequest.getUserId()).orElseThrow(
                () -> new ClinicNotFound("User not found")
        );

        scheduleEntity.setUser(user);

        DoctorEntity doctor = doctorService.getDoctorById(scheduleRequest.getDoctorId()).orElseThrow(
                () -> new DoctorNotFound("Доктор с таким ID не найден!")
        );
        scheduleEntity.setDoctor(doctor);

        scheduleEntity.setClinic(doctor.getClinic());

        return scheduleEntity;
    }

    @Override
    public ScheduleRecordRequest clone() {
        return new ScheduleRecordRequest(this);
    }
}

