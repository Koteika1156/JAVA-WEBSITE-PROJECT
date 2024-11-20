package com.example.demo.repository;

import com.example.demo.models.entity.ScheduleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ScheduleRepository extends CrudRepository<ScheduleEntity, Long> {
    Optional<ScheduleEntity> findByStartDateAndDoctorId(String startDate, String doctorId);
}
