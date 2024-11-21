package com.example.demo.models.response.schedule;

import com.example.demo.models.interfaces.ModelWithMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ScheduleRecordResponse extends ModelWithMessage {
    private boolean result;
}
