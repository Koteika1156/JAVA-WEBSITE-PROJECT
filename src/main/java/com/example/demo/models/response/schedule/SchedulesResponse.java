package com.example.demo.models.response.schedule;

import com.example.demo.models.interfaces.ModelWithMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class SchedulesResponse extends ModelWithMessage {
    private List<ScheduleResponse> schedules;
}
