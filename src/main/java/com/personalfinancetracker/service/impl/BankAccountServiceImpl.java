package com.personalfinancetracker.service.impl;

import com.personalfinancetracker.domain.dto.BankAccountDto;
import com.personalfinancetracker.domain.dto.UserDto;
import com.personalfinancetracker.domain.entities.BankAccountEntity;
import com.personalfinancetracker.domain.entities.UserEntity;
import com.personalfinancetracker.mapper.Mapper;
import com.personalfinancetracker.repositories.BankAccountRepository;
import com.personalfinancetracker.service.BankAccountService;
import com.personalfinancetracker.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import java.util.Optional;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final UserService userService;
    private final Mapper<UserEntity, UserDto> userMapper;
    private final Mapper<BankAccountEntity, BankAccountDto> bankAccountMapper;



    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository, UserService userService, Mapper<UserEntity, UserDto> userMapper, Mapper<BankAccountEntity, BankAccountDto> bankAccountMapper) {
        this.bankAccountRepository = bankAccountRepository;
        this.userService = userService;
        this.userMapper = userMapper;
        this.bankAccountMapper = bankAccountMapper;
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
    public Page<BankAccountEntity> findAllByUserId(Pageable pageable, Long userId) {
        return bankAccountRepository.findAllByUserId(pageable, userId);
    }

    @Override
    public boolean isExists(Long userId, Long bankAccountId) {
        return bankAccountRepository.existsById(userId, bankAccountId);
    }

    @Override
    public BankAccountEntity partialUpdate(BankAccountEntity bankAccountEntity) {
        return bankAccountRepository.findById(bankAccountEntity.getId()).map(existingAccount -> {
            Optional.ofNullable(bankAccountEntity.getName()).ifPresent(existingAccount::setName);
            Optional.ofNullable(bankAccountEntity.getBalance()).ifPresent(existingAccount::setBalance);
            existingAccount.setType(bankAccountEntity.getType());
            return bankAccountRepository.save(existingAccount);
        }).orElseThrow(() -> new RuntimeException("Bank Account Does Not Exist"));
    }

    public void fillBankAccountDtoWithUserAndDetails(BankAccountDto bankAccountDto, Long userId, Long bankAccountId) {
        Optional<UserEntity> foundUser = userService.findOne(userId);
        Optional<BankAccountEntity> foundAccount = bankAccountRepository.findById(bankAccountId);

        if (foundUser.isPresent() && foundAccount.isPresent()) {
            UserDto userDto = userMapper.mapTo(foundUser.get());
            BankAccountDto retrievedBankAccountDto = bankAccountMapper.mapTo(foundAccount.get());
            bankAccountDto.setUserDto(userDto);
            bankAccountDto.setType(retrievedBankAccountDto.getType());
            bankAccountDto.setId(retrievedBankAccountDto.getId());
        }
    }
}