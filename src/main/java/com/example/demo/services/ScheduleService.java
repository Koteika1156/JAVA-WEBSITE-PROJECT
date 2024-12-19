package com.example.demo.services;

import com.example.demo.models.request.schedule.ScheduleRecordRequest;
import com.example.demo.models.response.schedule.*;
import org.springframework.http.ResponseEntity;
import com.example.demo.models.request.schedule.ScheduleUpdateRequest;

public interface ScheduleService {
    ResponseEntity<ScheduleRecordResponse> addRecord(ScheduleRecordRequest request);

    ResponseEntity<ScheduleUpdateResponse> updateTime(ScheduleUpdateRequest recordUpdateRequest);

    ResponseEntity<ScheduleDeleteResponse> deleteRecord(String recordId);

    ResponseEntity<SchedulesResponse> getRecords();

    ResponseEntity<ScheduleResponse> getRecord(String recordId);
}
