package com.example.demo.models.request.schedule;

import com.example.demo.models.Prototype;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleUpdateRequest implements Prototype<ScheduleUpdateRequest> {
    private String id;
    private LocalDateTime newStartTime;
    private LocalDateTime newEndTime;

    public ScheduleUpdateRequest(ScheduleUpdateRequest scheduleUpdateRequest) {
        this.id = scheduleUpdateRequest.getId();
        this.newStartTime = scheduleUpdateRequest.getNewStartTime();
        this.newEndTime = scheduleUpdateRequest.getNewEndTime();
    }


    @Override
    public ScheduleUpdateRequest clone() {
        return new ScheduleUpdateRequest(this);
    }
}