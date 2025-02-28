package com.example.demo.repository;

import com.example.demo.models.entity.ScheduleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;


@Repository
public interface ScheduleRepository extends CrudRepository<ScheduleEntity, String> {
    Optional<ScheduleEntity> findByStartTimeAndDoctorId(LocalDateTime startTime, String doctorId);
    boolean existsByDoctorIdAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(String doctorId, LocalDateTime endTime, LocalDateTime startTime);
    boolean existsByDoctorIdAndStartTimeLessThanEqualAndEndTimeGreaterThanEqualAndIdNot(String doctorId, LocalDateTime endTime, LocalDateTime startTime, String Id);
    boolean existsByDoctorIdAndEndTimeGreaterThanAndStartTimeLessThan(String doctorId, LocalDateTime newStartTime, LocalDateTime newEndTime);
    boolean existsByDoctorIdAndEndTimeGreaterThanAndStartTimeLessThanAndIdNot(String doctorId, LocalDateTime newStartTime, LocalDateTime newEndTime, String Id);

}
