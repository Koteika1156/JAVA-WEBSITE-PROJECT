package com.example.demo.repository;

import com.example.demo.models.entity.UserEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("userRepositoryInterface")
public interface UserRepository extends CrudRepository<UserEntity, String> {

    UserEntity findByUsername(String username);
}
