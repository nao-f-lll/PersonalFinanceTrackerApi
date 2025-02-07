package com.personalfinancetracker.service.impl;

import com.personalfinancetracker.domain.dto.FinancialGoalDto;
import com.personalfinancetracker.domain.dto.UserDto;
import com.personalfinancetracker.domain.entities.BankAccountEntity;
import com.personalfinancetracker.domain.entities.FinancialGoalEntity;
import com.personalfinancetracker.domain.entities.UserEntity;
import com.personalfinancetracker.event.BankAccountCreateUpdatedEvent;
import com.personalfinancetracker.mapper.Mapper;
import com.personalfinancetracker.repositories.FinancialGoalRepository;
import com.personalfinancetracker.service.BankAccountService;
import com.personalfinancetracker.service.FinancialGoalService;
import com.personalfinancetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class FinancialGoalServiceImpl implements FinancialGoalService {

    private final FinancialGoalRepository financialGoalRepository;
    private final UserService userService;
    private final Mapper<UserEntity, UserDto> userMapper;
    private final BankAccountService bankAccountService;
    private final Mapper<FinancialGoalEntity, FinancialGoalDto> financialGoalMapper;
    private final String CACHE_TABLE_NAME = "userFinancialGoals";

    @Autowired
    public FinancialGoalServiceImpl(FinancialGoalRepository financialGoalRepository, UserService userService, Mapper<UserEntity, UserDto> userMapper, BankAccountService bankAccountService, Mapper<FinancialGoalEntity, FinancialGoalDto> financialGoalMapper) {
        this.financialGoalRepository = financialGoalRepository;
        this.userService = userService;
        this.userMapper = userMapper;
        this.bankAccountService = bankAccountService;
        this.financialGoalMapper = financialGoalMapper;
    }

    @CacheEvict(value = CACHE_TABLE_NAME, allEntries = true)
    @Override
    public FinancialGoalEntity create(FinancialGoalEntity financialGoalEntity) {
        Optional<UserEntity> userEntity = userService.findOne(financialGoalEntity.getUserEntity().getId());
        if (userEntity.isPresent()) {
            Double totalBalanceOfUserBankAccounts = getTotalBalance(userEntity.get().getId());
            financialGoalEntity.setCurrentAmount(totalBalanceOfUserBankAccounts);
            financialGoalEntity.setUserEntity(userEntity.get());
            return financialGoalRepository.save(financialGoalEntity);
        } else {
            return null;
        }
    }

    @Override
    public boolean isExists(Long financialGoalId) {
        return financialGoalRepository.existsById(financialGoalId);
    }
//REFACTOR DUPLICATED CODE
    @Override
    public void fillFinancialGoalDtoWithDetails(FinancialGoalDto financialGoalDto, Long financialGoalId) {
       Optional<FinancialGoalEntity> foundGoal = financialGoalRepository.findById(financialGoalId);
       Long userId = financialGoalRepository.findUserId(financialGoalId);
       Optional<UserEntity> foundUser = userService.findOne(userId);
        if (foundUser.isPresent() && foundGoal.isPresent()) {
            UserDto userDto = userMapper.mapTo(foundUser.get());
            FinancialGoalDto retrievedFinancialGoaDto = financialGoalMapper.mapTo(foundGoal.get());
            financialGoalDto.setUserDto(userDto);
            financialGoalDto.setId(retrievedFinancialGoaDto.getId());
            financialGoalDto.setCurrentAmount(retrievedFinancialGoaDto.getCurrentAmount());
            if (financialGoalDto.getExpectedDate() == null)
            {
                financialGoalDto.setExpectedDate(retrievedFinancialGoaDto.getExpectedDate());
            }
            if (financialGoalDto.getName() == null)
            {
                financialGoalDto.setName(retrievedFinancialGoaDto.getName());
            }
            if (financialGoalDto.getTargetAmount() == null)
            {
                financialGoalDto.setTargetAmount(retrievedFinancialGoaDto.getTargetAmount());
            }
        }
    }

    @CacheEvict(value = CACHE_TABLE_NAME, allEntries = true)
    @EventListener
    @Override
    public void updateCurrentAmount(BankAccountCreateUpdatedEvent event) {
        Long userId = event.getUserID();
        ArrayList<FinancialGoalEntity> financialGoalEntities = financialGoalRepository.findAll(userId);
        Double totalBalanceOfUserBankAccounts = getTotalBalance(userId);
        for (FinancialGoalEntity financialGoalEntity : financialGoalEntities) {
            financialGoalEntity.setCurrentAmount(totalBalanceOfUserBankAccounts);
            financialGoalRepository.save(financialGoalEntity);
        }
    }

    private Double getTotalBalance(Long userId) {
        ArrayList<BankAccountEntity> bankAccountEntities = bankAccountService.findAllByUserId(userId);
        Double totalBalanceOfUserBankAccounts = 0.0;
        for (BankAccountEntity bankAccountEntity : bankAccountEntities) {
            totalBalanceOfUserBankAccounts += bankAccountEntity.getBalance();
        }
        return totalBalanceOfUserBankAccounts;
    }

    @CacheEvict(value = CACHE_TABLE_NAME, allEntries = true)
    @Override
    public FinancialGoalEntity partialUpdate(FinancialGoalEntity financialGoalEntity) {
        return financialGoalRepository.findById(financialGoalEntity.getId()).map(existingGoal -> {
            Optional.ofNullable(financialGoalEntity.getExpectedDate()).ifPresent(existingGoal::setExpectedDate);
            Optional.ofNullable(financialGoalEntity.getName()).ifPresent(existingGoal::setName);
            Optional.ofNullable(financialGoalEntity.getTargetAmount()).ifPresent(existingGoal::setTargetAmount);
            return financialGoalRepository.save(existingGoal);
        }).orElseThrow(() -> new RuntimeException("Financial Goal Does Not Exist"));
    }

    @Override
    public void delete(Long financialGoalId) {
        financialGoalRepository.deleteById(financialGoalId);
    }

    @Cacheable(value = CACHE_TABLE_NAME, key = "#userId")
    @Override
    public Page<FinancialGoalEntity> findAllByUserId(Pageable pageable, Long userId) {
        return financialGoalRepository.findAll(pageable, userId);
    }
}