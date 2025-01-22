package com.example.demo.repository;

import com.example.demo.models.entity.ClinicEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicRepository extends CrudRepository<ClinicEntity, String> {
}
