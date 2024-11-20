package com.example.demo.services;

import com.example.demo.models.request.ScheduleRecordRequest;
import com.example.demo.models.response.ScheduleRecordResponse;
import org.springframework.http.ResponseEntity;

public interface ScheduleService {
    ResponseEntity<ScheduleRecordResponse> addRecord(ScheduleRecordRequest request);
}
