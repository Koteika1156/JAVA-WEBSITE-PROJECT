package com.example.demo.models.response.schedule;

import com.example.demo.models.entity.ScheduleEntity;
import com.example.demo.models.interfaces.ModelWithMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Objects;

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
                .userId(schedule.getUser() != null ? schedule.getUser().getId() : null)
                .hospitalId(schedule.getClinic() != null ? schedule.getClinic().getId() : null)
                .doctorId(schedule.getDoctor() != null ? schedule.getDoctor().getId() : null)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleResponse that = (ScheduleResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(doctorId, that.doctorId) && Objects.equals(userId, that.userId) && Objects.equals(hospitalId, that.hospitalId) && Objects.equals(startTime, that.startTime) && Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, doctorId, userId, hospitalId, startTime, endTime);
    }
}