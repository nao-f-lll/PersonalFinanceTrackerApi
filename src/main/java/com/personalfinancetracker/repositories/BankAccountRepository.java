package com.personalfinancetracker.repositories;

import com.personalfinancetracker.domain.entities.BankAccountEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends CrudRepository<BankAccountEntity, Long> {

    @Query("SELECT a from BankAccountEntity a where a.userEntity.id = ?1")
    Iterable<BankAccountEntity> findAllByUserId(Long userId);
}
