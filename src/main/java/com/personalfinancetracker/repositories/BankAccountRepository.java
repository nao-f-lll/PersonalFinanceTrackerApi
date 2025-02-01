package com.personalfinancetracker.repositories;

import com.personalfinancetracker.domain.entities.BankAccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface BankAccountRepository extends CrudRepository<BankAccountEntity, Long>,
        PagingAndSortingRepository<BankAccountEntity, Long> {

    @Query("SELECT a FROM BankAccountEntity a WHERE a.userEntity.id = :userId")
    Page<BankAccountEntity> findAllByUserId(Pageable pageable, @Param("userId") Long userId);

    @Query("SELECT a FROM BankAccountEntity a WHERE a.userEntity.id = :userId")
    ArrayList<BankAccountEntity> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT a.userEntity.id FROM BankAccountEntity a WHERE a.id = :bankAccountId")
    Long findUserId(@Param("bankAccountId") Long bankAccountId);

}