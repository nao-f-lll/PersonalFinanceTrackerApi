package com.personalfinancetracker.service.impl;

import com.personalfinancetracker.domain.entities.BankAccountEntity;
import com.personalfinancetracker.domain.entities.UserEntity;
import com.personalfinancetracker.repositories.BankAccountRepository;
import com.personalfinancetracker.service.BankAccountService;
import com.personalfinancetracker.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final UserService userService;


    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository, UserService userService) {
        this.bankAccountRepository = bankAccountRepository;
        this.userService = userService;
    }

    @Override
    public BankAccountEntity create(BankAccountEntity bankAccountEntity) {
        Optional<UserEntity> userEntity = userService.findOne(bankAccountEntity.getUserEntity().getId());
        if (userEntity.isPresent()) {
            bankAccountEntity.setUserEntity(userEntity.get());
            return bankAccountRepository.save(bankAccountEntity);
        }  else {
            return null;
        }
    }

    @Override
    public List<BankAccountEntity> findAllByUserId(Long userId) {
        return StreamSupport.stream(bankAccountRepository
                                .findAllByUserId(userId)
                                .spliterator(),
                        false)
                .collect(Collectors.toList());
    }

}
