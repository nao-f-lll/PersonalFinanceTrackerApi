package com.personalfinancetracker.service;

import com.personalfinancetracker.domain.entities.FinancialGoalEntity;
import org.springframework.stereotype.Component;

@Component
public interface FinancialGoalService {
    FinancialGoalEntity create(FinancialGoalEntity financialGoalEntity);
}
