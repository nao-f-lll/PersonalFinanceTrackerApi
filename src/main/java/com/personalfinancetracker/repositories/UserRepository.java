package com.personalfinancetracker.repositories;

import com.personalfinancetracker.domain.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    Boolean existsByEmail(String email);
}