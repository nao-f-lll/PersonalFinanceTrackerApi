package com.personalfinancetracker.personal_finance_tracker.repositories;

import com.personalfinancetracker.personal_finance_tracker.domain.entities.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    Boolean existsByEmail(String email);
}