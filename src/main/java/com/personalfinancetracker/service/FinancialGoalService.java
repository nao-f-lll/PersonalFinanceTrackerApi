package com.personalfinancetracker.service;

import com.personalfinancetracker.domain.dto.FinancialGoalDto;
import com.personalfinancetracker.domain.entities.FinancialGoalEntity;
import com.personalfinancetracker.event.BankAccountCreateUpdatedEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public interface FinancialGoalService {
    FinancialGoalEntity create(FinancialGoalEntity financialGoalEntity);

    boolean isExists(Long financialGoalId);

    void fillFinancialGoalDtoWithDetails(FinancialGoalDto financialGoalDto, Long financialGoalId);

    void updateCurrentAmount(BankAccountCreateUpdatedEvent event);

    FinancialGoalEntity partialUpdate(FinancialGoalEntity financialGoalEntity);

    void delete(Long financialGoalId);

    Page<FinancialGoalEntity> findAllByUserId(Pageable pageable, Long userId);
}
