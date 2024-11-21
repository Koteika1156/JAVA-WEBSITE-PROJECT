package com.example.demo.models.request.schedule;

import com.example.demo.models.entity.ScheduleEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleRecordRequest {
    private String doctorId;
    private String userId;
    private String hospitalId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public static ScheduleEntity toEntity(ScheduleRecordRequest scheduleRecordRequest) {
        ScheduleEntity scheduleEntity = new ScheduleEntity();

        scheduleEntity.setDoctorId(scheduleRecordRequest.getDoctorId());
        scheduleEntity.setUserId(scheduleRecordRequest.getUserId());
        scheduleEntity.setHospitalId(scheduleRecordRequest.getHospitalId());
        scheduleEntity.setStartTime(scheduleRecordRequest.getStartTime());
        scheduleEntity.setEndTime(scheduleRecordRequest.getEndTime());
        return scheduleEntity;
    }
}
