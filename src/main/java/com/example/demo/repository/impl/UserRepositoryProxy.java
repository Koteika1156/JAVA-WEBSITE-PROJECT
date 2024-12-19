package com.example.demo.repository.impl;

import com.example.demo.models.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Primary
@Component
// Паттерн proxy добавляем лоигрование по верх а так же ленивую инициализуцию
public class UserRepositoryProxy implements UserRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryProxy.class);

    private final UserRepository realUserRepository;

    private volatile UserRepository lazyRepository;

    public UserRepositoryProxy(@Qualifier("userRepositoryInterface") UserRepository realUserRepository) {
        this.realUserRepository = realUserRepository;
    }

    private UserRepository getLazyRepository() {
        if (lazyRepository == null) {
            synchronized (this) {
                if (lazyRepository == null) {
                    logger.info("Initializing heavy repository...");
                    lazyRepository = realUserRepository;
                    logger.info("Heavy repository initialized.");
                }
            }
        }
        return lazyRepository;
    }

    @Override
    public UserEntity findByUsername(String username) {
        logger.info("Proxy: Starting findByUsername for username={}", username);
        UserEntity user = getLazyRepository().findByUsername(username);
        logger.info("Proxy: Finished findByUsername, result={}", user);
        return user;
    }

    @Override
    public <S extends UserEntity> S save(S entity) {
        logger.info("Proxy: Starting save for entity={}", entity);
        S savedEntity = getLazyRepository().save(entity);
        logger.info("Proxy: Finished save for entity={}", savedEntity);
        return savedEntity;
    }

    @Override
    public <S extends UserEntity> Iterable<S> saveAll(Iterable<S> entities) {
        logger.info("Proxy: Starting saveAll for entities");
        Iterable<S> savedEntities = getLazyRepository().saveAll(entities);
        logger.info("Proxy: Finished saveAll");
        return savedEntities;
    }

    @Override
    public Optional<UserEntity> findById(String id) {
        logger.info("Proxy: Starting findById for id={}", id);
        Optional<UserEntity> result = getLazyRepository().findById(id);
        logger.info("Proxy: Finished findById, result={}", result);
        return result;
    }

    @Override
    public boolean existsById(String id) {
        logger.info("Proxy: Checking existence for id={}", id);
        boolean exists = getLazyRepository().existsById(id);
        logger.info("Proxy: Existence for id={} is {}", id, exists);
        return exists;
    }

    @Override
    public Iterable<UserEntity> findAll() {
        logger.info("Proxy: Starting findAll");
        Iterable<UserEntity> result = getLazyRepository().findAll();
        logger.info("Proxy: Finished findAll");
        return result;
    }

    @Override
    public Iterable<UserEntity> findAllById(Iterable<String> ids) {
        logger.info("Proxy: Starting findAllById for ids={}", ids);
        Iterable<UserEntity> result = getLazyRepository().findAllById(ids);
        logger.info("Proxy: Finished findAllById");
        return result;
    }

    @Override
    public long count() {
        logger.info("Proxy: Counting entities");
        long count = getLazyRepository().count();
        logger.info("Proxy: Total count={}", count);
        return count;
    }

    @Override
    public void deleteById(String id) {
        logger.info("Proxy: Starting deleteById for id={}", id);
        getLazyRepository().deleteById(id);
        logger.info("Proxy: Finished deleteById for id={}", id);
    }

    @Override
    public void delete(UserEntity entity) {
        logger.info("Proxy: Starting delete for entity={}", entity);
        getLazyRepository().delete(entity);
        logger.info("Proxy: Finished delete for entity={}", entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends String> ids) {
        logger.info("Proxy: Starting deleteAllById for ids={}", ids);
        getLazyRepository().deleteAllById(ids);
        logger.info("Proxy: Finished deleteAllById");
    }

    @Override
    public void deleteAll(Iterable<? extends UserEntity> entities) {
        logger.info("Proxy: Starting deleteAll for entities");
        getLazyRepository().deleteAll(entities);
        logger.info("Proxy: Finished deleteAll");
    }

    @Override
    public void deleteAll() {
        logger.info("Proxy: Starting deleteAll");
        getLazyRepository().deleteAll();
        logger.info("Proxy: Finished deleteAll");
    }
}
