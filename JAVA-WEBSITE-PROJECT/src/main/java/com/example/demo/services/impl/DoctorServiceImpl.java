package com.example.demo.services.impl;

import com.example.demo.exception.DoctorNotFound;
import com.example.demo.models.dto.DoctorDTO;
import com.example.demo.models.entity.DoctorEntity;
import com.example.demo.models.request.doctor.DoctorRequest;
import com.example.demo.models.response.doctor.DoctorAddResponse;
import com.example.demo.models.response.doctor.DoctorDeleteResponse;
import com.example.demo.models.response.doctor.DoctorResponse;
import com.example.demo.models.response.doctor.DoctorsResponse;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.services.ClinicService;
import com.example.demo.services.DoctorService;
import com.example.demo.util.strategy.ConcreteMultiFieldSortStrategy;
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
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private static final Logger logger = LoggerFactory.getLogger(DoctorServiceImpl.class);
    private final ClinicService clinicService;

    @Override
    public Optional<DoctorDTO> getDoctorDTOById(String id) {
        Optional<DoctorEntity> doctorEntity = doctorRepository.findById(id);

        return Optional.ofNullable(
                DoctorDTO.toDTO(doctorEntity.orElse(null))
        );
    }

    @Override
    public ResponseEntity<DoctorResponse> getDoctor(String id) {
        try {
            DoctorEntity doctorEntity = doctorRepository.findById(id).orElseThrow(
                    () -> new DoctorNotFound("Такого доктора нет!")
            );

            return ResponseEntity.ok()
                    .body(
                            DoctorResponse.toResponse(doctorEntity)
                    );
        } catch (DoctorNotFound e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest()
                    .body(
                            DoctorResponse.builder()
                                    .message("Ошибка получения доктора" + e.getMessage())
                                    .build()
                    );
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest()
                    .body(
                            DoctorResponse.builder()
                                    .message("Ошибка получения доктора")
                                    .build()
                    );
        }
    }

    @Override
    public ResponseEntity<DoctorsResponse> getAll() {
        try {
            List<DoctorEntity> doctors = (List<DoctorEntity>) doctorRepository.findAll();

            SortStrategy<DoctorResponse> strategy = new ConcreteMultiFieldSortStrategy<>(
                    List.of(DoctorResponse::getFirstName, DoctorResponse::getLastName),
                    false
            );

            return ResponseEntity.ok()
                    .body(
                            DoctorsResponse.builder()
                                    .strategy(strategy)
                                    .doctors(
                                            doctors.stream()
                                                    .map(DoctorResponse::toResponse)
                                                    .collect(Collectors.toList())
                                    )
                                    .build()
                                    .sortDoctors()
                    );
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public ResponseEntity<DoctorAddResponse> addDoctor(DoctorRequest doctorRequest) {
        try {
            DoctorEntity doctorEntity = DoctorRequest.toEntity(doctorRequest, clinicService);

            doctorRepository.save(doctorEntity);

            return ResponseEntity.ok()
                    .body(
                            DoctorAddResponse.builder()
                                    .result(true)
                                    .message("Успешно!")
                                    .build()
                    );
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest()
                    .body(
                            DoctorAddResponse.builder()
                                    .result(false)
                                    .message("Ошибка при добавлении доктора!")
                                    .build()
                    );
        }
    }

    @Override
    public ResponseEntity<DoctorDeleteResponse> deleteDoctor(String id) {
        try {
            doctorRepository.deleteById(id);

            return ResponseEntity.ok()
                    .body(
                            DoctorDeleteResponse.builder()
                                    .result(true)
                                    .message("Успешно!")
                                    .build()
                    );
        } catch (Exception e) {
            logger.error(e.getMessage());

            return ResponseEntity.badRequest()
                    .body(
                            DoctorDeleteResponse.builder()
                                    .result(false)
                                    .message("Ошибка при удалении доктора")
                                    .build()
                    );

        }
    }

    @Override
    public Optional<DoctorEntity> getDoctorById(String id) {
        return doctorRepository.findById(id);
    }
}
