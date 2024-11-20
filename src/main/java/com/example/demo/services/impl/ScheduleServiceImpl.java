package com.example.demo.services.impl;

import com.example.demo.exception.RecordAlreadyExist;
import com.example.demo.models.request.ScheduleRecordRequest;
import com.example.demo.models.response.ScheduleRecordResponse;
import com.example.demo.repository.ScheduleRepository;
import com.example.demo.services.ScheduleService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private static final Logger logger = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<ScheduleRecordResponse> addRecord(ScheduleRecordRequest scheduleRecordRequest) {
        try {

            if (scheduleRepository.findByStartDateAndDoctorId(scheduleRecordRequest.getStartDate(), scheduleRecordRequest.getDoctorId()).isPresent()) {
                throw new RecordAlreadyExist("Время занято");
            }

            scheduleRepository.save(ScheduleRecordRequest.toEntity(scheduleRecordRequest));

            return ResponseEntity
                    .ok(
                            ScheduleRecordResponse
                                    .builder()
                                    .result(true)
                                    .build()
                    );

        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body(
                            ScheduleRecordResponse
                                    .builder()
                                    .result(false)
                                    .build()
                    );
        }
    }
}