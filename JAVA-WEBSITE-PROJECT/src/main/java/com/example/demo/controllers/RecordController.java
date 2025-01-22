package com.example.demo.controllers;

import com.example.demo.models.request.schedule.ScheduleRecordRequest;
import com.example.demo.models.response.schedule.ScheduleDeleteResponse;
import com.example.demo.models.response.schedule.ScheduleRecordResponse;
import com.example.demo.models.response.schedule.ScheduleResponse;
import com.example.demo.models.response.schedule.SchedulesResponse;
import com.example.demo.services.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.models.request.schedule.ScheduleUpdateRequest;
import com.example.demo.models.response.schedule.ScheduleUpdateResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/record")
public class RecordController {
    private final ScheduleService scheduleService;

    @GetMapping("/all")
    public ResponseEntity<SchedulesResponse> getRecords() {
        return scheduleService.getRecords();
    }

    @GetMapping("/getRecord")
    public ResponseEntity<ScheduleResponse> getRecord(@RequestParam String recordId) {
        return scheduleService.getRecord(recordId);
    }

    @PostMapping("/add")
    public ResponseEntity<ScheduleRecordResponse> addRecord(@RequestBody ScheduleRecordRequest request) {
        return scheduleService.addRecord(request);
    }

    @PutMapping("/update")
    public ResponseEntity<ScheduleUpdateResponse> updateTime(@RequestBody ScheduleUpdateRequest recordUpdateRequest) {
        return scheduleService.updateTime(recordUpdateRequest);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ScheduleDeleteResponse> deleteRecord(@RequestParam String recordId) {
        return scheduleService.deleteRecord(recordId);
    }
}
