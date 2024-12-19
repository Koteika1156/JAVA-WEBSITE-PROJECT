package com.example.demo.repository;

import com.example.demo.models.entity.DoctorEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends CrudRepository<DoctorEntity, String> {
}
