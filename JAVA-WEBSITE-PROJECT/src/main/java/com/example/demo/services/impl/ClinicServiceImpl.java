package com.example.demo.services.impl;

import com.example.demo.exception.ClinicNotFound;
import com.example.demo.models.entity.ClinicEntity;
import com.example.demo.models.request.clinic.ClinicAddRequest;
import com.example.demo.models.response.clinic.ClinicAddResponse;
import com.example.demo.models.response.clinic.ClinicDeleteResponse;
import com.example.demo.models.response.clinic.ClinicResponse;
import com.example.demo.models.response.clinic.ClinicsResponse;
import com.example.demo.repository.ClinicRepository;
import com.example.demo.services.ClinicService;
import com.example.demo.util.strategy.ConcreteOneFieldSortStrategy;
import com.example.demo.util.strategy.SortStrategy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClinicServiceImpl implements ClinicService {
    private final ClinicRepository clinicRepository;
    private static final Logger logger = LoggerFactory.getLogger(ClinicServiceImpl.class);

    public Optional<ClinicEntity> getClinicById(String id) {
        return clinicRepository.findById(id);
    }

    @Override
    public ResponseEntity<ClinicResponse> getClinic(String clinicId) {
        try {
            ClinicEntity clinic = clinicRepository.findById(clinicId).orElseThrow(
                    () -> new ClinicNotFound("Клиника не найдена!")
            );

            return ResponseEntity.ok(
                    ClinicResponse
                            .toResponse(clinic)
            );
        } catch (Exception e) {
            logger.error(e.getMessage());

            return ResponseEntity
                    .badRequest()
                    .body(
                            ClinicResponse
                                    .builder()
                                    .message("Ошибка получения клиники")
                                    .build()
                    );
        }
    }

    @Override
    public ResponseEntity<ClinicsResponse> getClinics() {
        try {
            List<ClinicEntity> clinicEntities = (List<ClinicEntity>) clinicRepository.findAll();

            SortStrategy<ClinicResponse> strategy = new ConcreteOneFieldSortStrategy<>(
                    ClinicResponse::getName,
                    false
            );

            return ResponseEntity
                    .ok(
                            ClinicsResponse.builder()
                                    .strategy(strategy)
                                    .clinics(
                                            clinicEntities
                                                    .stream()
                                                    .map(ClinicResponse::toResponse)
                                                    .collect(Collectors.toList())
                                    )
                                    .message("Успешно!")
                                    .build()
                                    .sortClinics()
                    );
        } catch (Exception e) {
            logger.error(e.getMessage());

            return ResponseEntity
                    .badRequest()
                    .body(
                            ClinicsResponse.builder()
                                    .clinics(List.of())
                                    .message("Ошибка получения клиник")
                                    .build()
                    );
        }
    }

    @Override
    public ResponseEntity<ClinicAddResponse> addClinic(ClinicAddRequest clinicAddRequest) {
        try {
            ClinicEntity clinic = ClinicAddRequest.toEntity(clinicAddRequest);

            clinicRepository.save(clinic);

            return ResponseEntity.ok()
                    .body(
                            ClinicAddResponse.builder()
                                    .message("Успешно!")
                                    .result(true)
                                    .build()
                    );
        } catch (Exception e) {
            logger.error(e.getMessage());

            return ResponseEntity.badRequest()
                    .body(
                            ClinicAddResponse.builder()
                                    .message("Не удалось добавить клинику")
                                    .result(false)
                                    .build()
                    );
        }
    }

    @Override
    public ResponseEntity<ClinicDeleteResponse> deleteClinic(String clinicId) {
        try {
            clinicRepository.deleteById(clinicId);
            return ResponseEntity
                    .ok()
                    .body(
                            ClinicDeleteResponse.builder()
                                    .message("Успешно!")
                                    .result(true)
                                    .build()
                    );
        } catch (Exception e) {
            logger.error(e.getMessage());

            return ResponseEntity
                    .badRequest()
                    .body(
                            ClinicDeleteResponse.builder()
                                    .message("Ошибка удаления клиники")
                                    .result(false)
                                    .build()
                    );
        }
    }
}
