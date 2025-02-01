package com.personalfinancetracker.service.impl;

import com.personalfinancetracker.domain.dto.BankAccountDto;
import com.personalfinancetracker.domain.dto.UserDto;
import com.personalfinancetracker.domain.entities.BankAccountEntity;
import com.personalfinancetracker.domain.entities.UserEntity;
import com.personalfinancetracker.event.BankAccountCreateUpdatedEvent;
import com.personalfinancetracker.mapper.Mapper;
import com.personalfinancetracker.repositories.BankAccountRepository;
import com.personalfinancetracker.service.BankAccountService;
import com.personalfinancetracker.service.UserService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final UserService userService;
    private final Mapper<UserEntity, UserDto> userMapper;
    private final Mapper<BankAccountEntity, BankAccountDto> bankAccountMapper;
    private final String CACHE_TABLE_NAME = "userBankAccounts";
    private final ApplicationEventPublisher eventPublisher;


    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository, UserService userService, Mapper<UserEntity, UserDto> userMapper, Mapper<BankAccountEntity, BankAccountDto> bankAccountMapper, ApplicationEventPublisher eventPublisher) {
        this.bankAccountRepository = bankAccountRepository;
        this.userService = userService;
        this.userMapper = userMapper;
        this.bankAccountMapper = bankAccountMapper;
        this.eventPublisher = eventPublisher;
    }

    @CacheEvict(value = CACHE_TABLE_NAME, allEntries = true)
    @Override
    public BankAccountEntity create(BankAccountEntity bankAccountEntity) {
        Optional<UserEntity> userEntity = userService.findOne(bankAccountEntity.getUserEntity().getId());
        if (userEntity.isPresent()) {
            bankAccountEntity.setUserEntity(userEntity.get());
            BankAccountEntity saveBAnkAccount = bankAccountRepository.save(bankAccountEntity);
            BankAccountCreateUpdatedEvent event = new BankAccountCreateUpdatedEvent(this, bankAccountEntity.getUserEntity().getId());
            eventPublisher.publishEvent(event);
            return saveBAnkAccount;
        }  else {
            return null;
        }
    }

    @Cacheable(value = CACHE_TABLE_NAME, key = "#userId")
    @Override
    public Page<BankAccountEntity> findAllByUserId(Pageable pageable, Long userId) {
        return  bankAccountRepository.findAllByUserId(pageable, userId);
    }

    @Override
    public ArrayList<BankAccountEntity> findAllByUserId(Long userId) {
        return bankAccountRepository.findAllByUserId(userId);
    }

    @Override
    public boolean isExists(Long bankAccountId) {
        return bankAccountRepository.existsById(bankAccountId);
    }

    @CacheEvict(value = CACHE_TABLE_NAME, allEntries = true)
    @Override
    public BankAccountEntity partialUpdate(BankAccountEntity bankAccountEntity) {
        return bankAccountRepository.findById(bankAccountEntity.getId()).map(existingAccount -> {
            Optional.ofNullable(bankAccountEntity.getName()).ifPresent(existingAccount::setName);
            Optional.ofNullable(bankAccountEntity.getBalance()).ifPresent(existingAccount::setBalance);
            Optional.ofNullable(bankAccountEntity.getType()).ifPresent(existingAccount::setType);
            BankAccountEntity saveBAnkAccount = bankAccountRepository.save(existingAccount);
            BankAccountCreateUpdatedEvent event = new BankAccountCreateUpdatedEvent(this, bankAccountEntity.getUserEntity().getId());
            eventPublisher.publishEvent(event);
            return saveBAnkAccount;
        }).orElseThrow(() -> new RuntimeException("Bank Account Does Not Exist"));
    }

    public void fillBankAccountDtoWithDetails(BankAccountDto bankAccountDto, Long bankAccountId) {
        Optional<BankAccountEntity> foundAccount = bankAccountRepository.findById(bankAccountId);
        Long userId = bankAccountRepository.findUserId(bankAccountId);
        Optional<UserEntity> foundUser = userService.findOne(userId);

        if (foundUser.isPresent() && foundAccount.isPresent()) {
            UserDto userDto = userMapper.mapTo(foundUser.get());
            BankAccountDto retrievedBankAccountDto = bankAccountMapper.mapTo(foundAccount.get());
            bankAccountDto.setUserDto(userDto);
            if (bankAccountDto.getType() == null)
            {
                bankAccountDto.setType(retrievedBankAccountDto.getType());
            }
            if (bankAccountDto.getBalance() == null)
            {
                bankAccountDto.setBalance(retrievedBankAccountDto.getBalance());
            }
            bankAccountDto.setId(retrievedBankAccountDto.getId());
        }
    }

    @Override
    public void delete(Long bankAccountId, Long userId) {
        bankAccountRepository.deleteById(bankAccountId);
        BankAccountCreateUpdatedEvent event = new BankAccountCreateUpdatedEvent(this, userId);
        eventPublisher.publishEvent(event);
    }

    @Override
    public Optional<BankAccountEntity> findOne(Long bankAccountId) {
        return bankAccountRepository.findById(bankAccountId);
    }
}

