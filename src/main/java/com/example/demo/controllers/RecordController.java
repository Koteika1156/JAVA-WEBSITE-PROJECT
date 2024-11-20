package com.example.demo.controllers;

import com.example.demo.models.request.ScheduleRecordRequest;
import com.example.demo.models.response.ScheduleRecordResponse;
import com.example.demo.services.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1")
public class RecordController {
    private final ScheduleService scheduleService;

    public RecordController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/addRecord")
    public ResponseEntity<ScheduleRecordResponse> addRecord(@RequestBody ScheduleRecordRequest request) {

        return scheduleService.addRecord(request);
    }
}
