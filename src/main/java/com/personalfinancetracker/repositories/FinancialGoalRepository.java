package com.personalfinancetracker.repositories;

import com.personalfinancetracker.domain.entities.FinancialGoalEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface FinancialGoalRepository extends CrudRepository<FinancialGoalEntity, Long>,
        PagingAndSortingRepository<FinancialGoalEntity, Long> {

    @Query("SELECT a.userEntity.id FROM FinancialGoalEntity a WHERE a.id = :financialGoalId")
    Long findUserId(@Param("financialGoalId") Long financialGoalId);

    @Query("SELECT a from FinancialGoalEntity a WHERE a.userEntity.id = :userId")
    ArrayList<FinancialGoalEntity> findAll(@Param("userId") Long userId);

    @Query("SELECT a from FinancialGoalEntity a WHERE a.userEntity.id = :userId")
    Page<FinancialGoalEntity> findAll(Pageable pageable, @Param("userId") Long userId);
}