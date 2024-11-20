package com.example.demo.models.request;

import com.example.demo.models.entity.ScheduleEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleRecordRequest {
    private String doctorId;
    private String userId;
    private String hospitalId;
    private String startDate;
    private String endDate;

    public static ScheduleEntity toEntity(ScheduleRecordRequest scheduleRecordRequest) {
        ScheduleEntity scheduleEntity = new ScheduleEntity();

        scheduleEntity.setDoctorId(scheduleRecordRequest.getDoctorId());
        scheduleEntity.setUserId(scheduleRecordRequest.getUserId());
        scheduleEntity.setHospitalId(scheduleRecordRequest.getHospitalId());
        scheduleEntity.setStartDate(scheduleRecordRequest.getStartDate());
        scheduleEntity.setEndDate(scheduleRecordRequest.getEndDate());
        return scheduleEntity;
    }
}
