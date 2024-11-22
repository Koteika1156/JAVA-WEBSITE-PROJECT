package com.example.demo.models.request.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleUpdateRequest {
    private String id;
    private LocalDateTime newStartTime;
    private LocalDateTime newEndTime;
}