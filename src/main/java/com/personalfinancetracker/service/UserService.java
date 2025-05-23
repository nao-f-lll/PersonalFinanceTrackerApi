package com.personalfinancetracker.service;

import com.personalfinancetracker.domain.entities.UserEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserService {
    UserEntity create(UserEntity userEntity);

    Boolean isExists(String email);

    Boolean isExists(Long id);

    UserEntity fullUpdate(UserEntity userEntity);

    UserEntity partialUpdate(UserEntity userEntity);

    void delete(Long id);

    Optional<UserEntity> findOne(Long id);
}