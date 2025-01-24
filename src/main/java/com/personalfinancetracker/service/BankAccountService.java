package com.personalfinancetracker.service;

import com.personalfinancetracker.domain.entities.BankAccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BankAccountService {
    BankAccountEntity create(BankAccountEntity bankAccountEntity);

    List<BankAccountEntity> findAllByUserId(Long userId);
}
