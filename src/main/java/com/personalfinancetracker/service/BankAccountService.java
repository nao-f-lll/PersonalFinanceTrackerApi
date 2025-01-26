package com.personalfinancetracker.service;

import com.personalfinancetracker.domain.dto.BankAccountDto;
import com.personalfinancetracker.domain.entities.BankAccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public interface BankAccountService {
    BankAccountEntity create(BankAccountEntity bankAccountEntity);

    Page<BankAccountEntity> findAllByUserId(Pageable pageable, Long userId);

    boolean isExists(Long userId, Long bankAccountId);

    BankAccountEntity partialUpdate(BankAccountEntity bankAccountEntity);

    void fillBankAccountDtoWithUserAndDetails(BankAccountDto bankAccountDto, Long userId, Long bankAccountId);

    void delete(Long bankAccountId);
}