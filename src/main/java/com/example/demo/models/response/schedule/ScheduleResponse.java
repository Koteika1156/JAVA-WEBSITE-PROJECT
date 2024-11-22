package com.example.demo.models.response.schedule;

import com.example.demo.models.entity.ScheduleEntity;
import com.example.demo.models.interfaces.ModelWithMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
public class ScheduleResponse extends ModelWithMessage {
    private String id;
    private String doctorId;
    private String userId;
    private String hospitalId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public static ScheduleResponse toResponse(ScheduleEntity schedule) {
        if (schedule == null) {
            return null;
        }

        return ScheduleResponse
                .builder()
                .id(schedule.getId())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .hospitalId(schedule.getClinic().getId())
                .doctorId(schedule.getDoctor().getId())
                .build();
    }
}