package com.personalfinancetracker.controllers;

import com.personalfinancetracker.domain.dto.BankAccountDto;
import com.personalfinancetracker.domain.entities.BankAccountEntity;
import com.personalfinancetracker.exceptions.ErrorResponse;
import com.personalfinancetracker.mapper.Mapper;
import com.personalfinancetracker.service.BankAccountService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class BankAccountController {

    private static final Logger logger = LoggerFactory.getLogger(BankAccountController.class);
    private final BankAccountService bankAccountService;
    private final Mapper<BankAccountEntity, BankAccountDto> bankAccountMapper;


    @Autowired
    public BankAccountController(Mapper<BankAccountEntity, BankAccountDto> bankAccountMapper, BankAccountService bankAccountService) {
        this.bankAccountMapper = bankAccountMapper;
        this.bankAccountService = bankAccountService;
    }

    @GetMapping(path = "/v1/bank-accounts/{user-id}")
    public Page<BankAccountDto> getBankAccounts(Pageable pageable, @PathVariable("user-id") Long userId) {
        Page<BankAccountEntity> bankAccountEntities = bankAccountService.findAllByUserId(pageable, userId);
        return bankAccountEntities.map(bankAccountMapper::mapTo);
    }

    @PostMapping(path = "/v1/bank-accounts")
    public ResponseEntity<Object> createBankAccount(@Valid @RequestBody BankAccountDto bankAccountDto) {
        try {
            BankAccountEntity bankAccountEntity = bankAccountMapper.mapFrom(bankAccountDto);
            BankAccountEntity saveBankAccount = bankAccountService.create(bankAccountEntity);
            if (saveBankAccount != null) {
                BankAccountDto response = bankAccountMapper.mapTo(saveBankAccount);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("The user does not exists",HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(path = "/v1/bank-accounts/{user-id}/{bank-account-id}")
    public ResponseEntity<Object> partialUpdate(@RequestBody BankAccountDto bankAccountDto,
                                                @PathVariable("user-id") Long userId, @PathVariable("bank-account-id") Long bankAccountId) {
        ResponseEntity<Object> errorResponse = bankAccountDoesNotExistsException(userId, bankAccountId);
        if (errorResponse != null) return errorResponse;
        bankAccountService.fillBankAccountDtoWithUserAndDetails(bankAccountDto, userId, bankAccountId);
        try {
            BankAccountEntity bankAccountEntity = bankAccountMapper.mapFrom(bankAccountDto);
            BankAccountEntity saveBankAccount = bankAccountService.partialUpdate(bankAccountEntity);
            BankAccountDto response = bankAccountMapper.mapTo(saveBankAccount);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<Object> bankAccountDoesNotExistsException(Long userId, Long bankAccountId) {
        if (!bankAccountService.isExists(userId, bankAccountId)) {
            ErrorResponse errorResponse = ErrorResponse
                    .builder()
                    .message("Bank Account Does Not Exists")
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        return null;
    }
}