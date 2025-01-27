package com.personalfinancetracker.repositories;

import com.personalfinancetracker.domain.entities.BankAccountEntity;
import com.personalfinancetracker.domain.entities.FinancialGoalEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinancialGoalRepository extends CrudRepository<FinancialGoalEntity, Long>,
        PagingAndSortingRepository<FinancialGoalEntity, Long> {

}