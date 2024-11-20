package com.example.demo.repository;

import com.example.demo.models.entity.ClinicEntity;
import org.springframework.data.repository.CrudRepository;

public interface ClinicRepository extends CrudRepository<ClinicEntity, String> {
}
