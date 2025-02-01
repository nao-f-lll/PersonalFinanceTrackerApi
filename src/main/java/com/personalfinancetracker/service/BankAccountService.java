package com.personalfinancetracker.service;

import com.personalfinancetracker.domain.dto.BankAccountDto;
import com.personalfinancetracker.domain.entities.BankAccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import java.util.Optional;


import java.util.ArrayList;

@Component
public interface BankAccountService {
    BankAccountEntity create(BankAccountEntity bankAccountEntity);

    Page<BankAccountEntity> findAllByUserId(Pageable pageable, Long userId);

    ArrayList<BankAccountEntity> findAllByUserId(Long userId);


    boolean isExists(Long bankAccountId);

    BankAccountEntity partialUpdate(BankAccountEntity bankAccountEntity);

    void fillBankAccountDtoWithDetails(BankAccountDto bankAccountDto, Long bankAccountId);

    void delete(Long bankAccountId, Long userId);

    Optional<BankAccountEntity> findOne(Long bankAccountId);
}