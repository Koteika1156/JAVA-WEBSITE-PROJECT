package com.example.demo.controllers;

import com.example.demo.models.request.schedule.ScheduleRecordRequest;
import com.example.demo.models.response.schedule.ScheduleDeleteResponse;
import com.example.demo.models.response.schedule.ScheduleRecordResponse;
import com.example.demo.models.response.schedule.ScheduleResponse;
import com.example.demo.models.response.schedule.SchedulesResponse;
import com.example.demo.services.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.models.request.schedule.ScheduleUpdateRequest;
import com.example.demo.models.response.schedule.ScheduleUpdateResponse;

@RestController
@RequestMapping("/api/v1/record")
public class RecordController {
    private final ScheduleService scheduleService;

    public RecordController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/add")
    public ResponseEntity<ScheduleRecordResponse> addRecord(@RequestBody ScheduleRecordRequest request) {
        return scheduleService.addRecord(request);
    }

    @PostMapping("/update")
    public ResponseEntity<ScheduleUpdateResponse> updateTime(@RequestBody ScheduleUpdateRequest recordUpdateRequest) {
        return scheduleService.updateTime(recordUpdateRequest);
    }

    @PostMapping("/delete")
    public ResponseEntity<ScheduleDeleteResponse> deleteRecord(@RequestParam String recordId) {
        return scheduleService.deleteRecord(recordId);
    }

    @GetMapping("/all")
    public ResponseEntity<SchedulesResponse> getRecords() {
        return scheduleService.getRecords();
    }

    @GetMapping("/getRecord")
    public ResponseEntity<ScheduleResponse> getRecord(@RequestParam String recordId) {
        return scheduleService.getRecord(recordId);
    }

}