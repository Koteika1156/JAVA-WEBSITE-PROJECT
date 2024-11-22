package com.example.demo.services.impl;

import com.example.demo.exception.*;
import com.example.demo.models.entity.ClinicEntity;
import com.example.demo.models.entity.DoctorEntity;
import com.example.demo.models.entity.ScheduleEntity;
import com.example.demo.models.request.schedule.ScheduleRecordRequest;
import com.example.demo.models.request.schedule.ScheduleUpdateRequest;
import com.example.demo.models.response.schedule.*;
import com.example.demo.repository.ScheduleRepository;
import com.example.demo.services.ClinicService;
import com.example.demo.services.DoctorService;
import com.example.demo.services.ScheduleService;
import com.example.demo.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ClinicService clinicService;
    private final DoctorService doctorService;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    ScheduleServiceImpl(ScheduleRepository scheduleRepository, DoctorService doctorService, ClinicService clinicService, UserService userService) {
        this.scheduleRepository = scheduleRepository;
        this.clinicService = clinicService;
        this.doctorService = doctorService;
        this.userService = userService;
    }

    @Override
    public ResponseEntity<ScheduleRecordResponse> addRecord(ScheduleRecordRequest scheduleRecordRequest) {
        try {

            if (scheduleRepository.findByStartTimeAndDoctorId(scheduleRecordRequest.getStartTime(), scheduleRecordRequest.getDoctorId()).isPresent()) {
                throw new RecordAlreadyExist("Время занято");
            }

            DoctorEntity doctor = doctorService.getDoctorById(scheduleRecordRequest.getDoctorId()).orElseThrow(
                    () -> new DoctorNotFound("Доктор с таким ID не найден!")
            );

            ClinicEntity clinic = clinicService.getClinicById(doctor.getClinic().getId())
                    .orElseThrow(() -> new ClinicNotFound("Клиника не найдена"));

            if (!scheduleRecordRequest.getStartTime().toLocalDate().equals(scheduleRecordRequest.getEndTime().toLocalDate())) {
                throw new DifferentDatesException("Временной промежуток задевает разные даты");
            }

            if (scheduleRecordRequest.getStartTime().toLocalTime().isBefore(clinic.getOpenTime()) ||
                    scheduleRecordRequest.getEndTime().toLocalTime().isAfter(clinic.getCloseTime())) {
                throw new OutsideClinicHoursException("Время вне рабочего времени клиники");
            }

            if (scheduleRepository.existsByDoctorIdAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(scheduleRecordRequest.getDoctorId(), scheduleRecordRequest.getStartTime(), scheduleRecordRequest.getEndTime())
            || scheduleRepository.existsByDoctorIdAndEndTimeGreaterThanAndStartTimeLessThan(scheduleRecordRequest.getDoctorId(), scheduleRecordRequest.getStartTime(), scheduleRecordRequest.getEndTime())) {
                throw new CrossingTimeException("Время пересекается с другой записью!");
            }

            ScheduleEntity scheduleEntity = ScheduleRecordRequest.toEntity(scheduleRecordRequest, doctorService, userService);
            scheduleRepository.save(scheduleEntity);

            return ResponseEntity
                    .ok(
                            ScheduleRecordResponse
                                    .builder()
                                    .result(true)
                                    .message("Успешно!")
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
                                    .message(e.getMessage())
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

            ClinicEntity clinic = clinicService.getClinicById(schedule.getId())
                    .orElseThrow(() -> new RecordNotFound("Клиника не найдена"));

            if (recordUpdateRequest.getNewStartTime().toLocalTime().isBefore(clinic.getOpenTime()) ||
                    recordUpdateRequest.getNewEndTime().toLocalTime().isAfter(clinic.getCloseTime())) {
                throw new OutsideClinicHoursException("Время вне рабочего времени клиники");
            }

            if (!recordUpdateRequest.getNewStartTime().toLocalDate().equals(recordUpdateRequest.getNewEndTime().toLocalDate())) {
                throw new DifferentDatesException("Временной промежуток задевает разные даты");
            }

            if (scheduleRepository.existsByDoctorIdAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(schedule.getDoctor().getId(), recordUpdateRequest.getNewStartTime(), recordUpdateRequest.getNewEndTime())
                    || scheduleRepository.existsByDoctorIdAndEndTimeGreaterThanAndStartTimeLessThan(schedule.getDoctor().getId(), recordUpdateRequest.getNewStartTime(), recordUpdateRequest.getNewEndTime())) {
                throw new CrossingTimeException("Время пересекается с другой записью!");
            }

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