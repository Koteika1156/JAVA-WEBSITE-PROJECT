package com.example.demo.models.response.schedule;

import com.example.demo.models.entity.ScheduleEntity;
import com.example.demo.models.interfaces.ModelWithMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ScheduleResponse extends ModelWithMessage {
    private String id;
    private String doctorId;
    private String userId;
    private String hospitalId;
    private String startTime;
    private String endTime;

    public static ScheduleResponse toResponse(ScheduleEntity schedule) {
        if (schedule == null) {
            return null;
        }

        return ScheduleResponse
                .builder()
                .id(schedule.getId())
                .doctorId(schedule.getDoctorId())
                .userId(schedule.getUserId())
                .hospitalId(schedule.getHospitalId())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .build();
    }
}