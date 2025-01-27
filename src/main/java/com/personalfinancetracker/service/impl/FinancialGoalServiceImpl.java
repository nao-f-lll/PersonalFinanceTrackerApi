package com.personalfinancetracker.service.impl;

import com.personalfinancetracker.domain.entities.BankAccountEntity;
import com.personalfinancetracker.domain.entities.FinancialGoalEntity;
import com.personalfinancetracker.domain.entities.UserEntity;
import com.personalfinancetracker.repositories.FinancialGoalRepository;
import com.personalfinancetracker.service.BankAccountService;
import com.personalfinancetracker.service.FinancialGoalService;
import com.personalfinancetracker.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class FinancialGoalServiceImpl implements FinancialGoalService {

    private final FinancialGoalRepository financialGoalRepository;
    private final UserService userService;
    private final BankAccountService bankAccountService;

    public FinancialGoalServiceImpl(FinancialGoalRepository financialGoalRepository, UserService userService, BankAccountService bankAccountService) {
        this.financialGoalRepository = financialGoalRepository;
        this.userService = userService;
        this.bankAccountService = bankAccountService;
    }

    @Override
    public FinancialGoalEntity create(FinancialGoalEntity financialGoalEntity) {
        Optional<UserEntity> userEntity = userService.findOne(financialGoalEntity.getUserEntity().getId());
        if (userEntity.isPresent()) {
            ArrayList<BankAccountEntity> bankAccountEntities = bankAccountService.findAllByUserId(userEntity.get().getId());
            Double totalBalanceOfUserBankAccounts = 0.0;
            for (BankAccountEntity bankAccountEntity : bankAccountEntities) {
                totalBalanceOfUserBankAccounts += bankAccountEntity.getBalance();
            }
            financialGoalEntity.setCurrentAmount(totalBalanceOfUserBankAccounts);
            financialGoalEntity.setUserEntity(userEntity.get());
            return financialGoalRepository.save(financialGoalEntity);
        } else {
            return null;
        }
    }
}