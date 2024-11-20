package com.example.demo.services.impl;

import com.example.demo.exception.RecordAlreadyExist;
import com.example.demo.exception.RecordNotFound;
import com.example.demo.models.entity.ScheduleEntity;
import com.example.demo.models.request.schedule.ScheduleRecordRequest;
import com.example.demo.models.request.schedule.ScheduleUpdateRequest;
import com.example.demo.models.response.schedule.*;
import com.example.demo.repository.ScheduleRepository;
import com.example.demo.services.ScheduleService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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

            if (scheduleRepository.findByStartTimeAndDoctorId(scheduleRecordRequest.getStartTime(), scheduleRecordRequest.getDoctorId()).isPresent()) {
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

    @Override
    public ResponseEntity<ScheduleUpdateResponse> updateTime(ScheduleUpdateRequest recordUpdateRequest) {
        try {

            if (recordUpdateRequest.getId() == null || recordUpdateRequest.getNewStartTime() == null|| recordUpdateRequest.getNewEndTime() == null) {
                return ResponseEntity
                        .badRequest()
                        .body(
                                ScheduleUpdateResponse
                                        .builder()
                                        .result(false)
                                        .message("Ошибка в обновленных данных записи")
                                        .build()
                        );
            }

            ScheduleEntity schedule = scheduleRepository.findById(recordUpdateRequest.getId()).orElseThrow(
                    () -> new RecordNotFound("Запись не найдена!")
            );

            schedule.setStartTime(recordUpdateRequest.getNewStartTime());
            schedule.setEndTime(recordUpdateRequest.getNewEndTime());

            scheduleRepository.save(schedule);

            return ResponseEntity
                    .ok()
                    .body(
                            ScheduleUpdateResponse
                                    .builder()
                                    .result(true)
                                    .message("Запись успешно обновлена")
                                    .build()
                    );
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body(
                            ScheduleUpdateResponse
                                    .builder()
                                    .result(false)
                                    .message("Ошибка при обновлении записи")
                                    .build()
                    );
        }
    }

    @Override
    public ResponseEntity<ScheduleDeleteResponse> deleteRecord(String recordId) {
        try {
            scheduleRepository.deleteById(recordId);
            return ResponseEntity
                    .ok()
                    .body(
                            ScheduleDeleteResponse.builder()
                                    .message("Успешно!")
                                    .result(true)
                                    .build()
                    );
        } catch (Exception e) {
            logger.error(e.getMessage());

            return ResponseEntity
                    .badRequest()
                    .body(
                            ScheduleDeleteResponse.builder()
                                    .message("Ошибка удаления записи")
                                    .result(false)
                                    .build()
                    );
        }
    }

    @Override
    public ResponseEntity<SchedulesResponse> getRecords() {
        try {
            List<ScheduleEntity> scheduleEntities = (List<ScheduleEntity>) scheduleRepository.findAll();

            return ResponseEntity
                    .ok(
                            SchedulesResponse.builder()
                                    .schedules(
                                            scheduleEntities
                                                    .stream()
                                                    .map(ScheduleResponse::toResponse)
                                                    .toList()
                                    )
                                    .message("Успешно!")
                                    .build()

                    );
        } catch (Exception e) {
            logger.error(e.getMessage());

            return ResponseEntity
                    .badRequest()
                    .body(
                            SchedulesResponse.builder()
                                    .schedules(List.of())
                                    .message("Ошибка получения записей")
                                    .build()
                    );
        }
    }

    @Override
    public ResponseEntity<ScheduleResponse> getRecord(String recordId) {
        try {
            ScheduleEntity schedule = scheduleRepository.findById(recordId).orElseThrow(
                    () -> new RecordNotFound("Запись не найдена!")
            );

            return ResponseEntity.ok(
                    ScheduleResponse
                            .toResponse(schedule)
            );
        } catch (Exception e) {
            logger.error(e.getMessage());

            return ResponseEntity
                    .badRequest()
                    .body(
                            ScheduleResponse
                                    .builder()
                                    .message("Ошибка получения записи")
                                    .build()
                    );
        }
    }
}