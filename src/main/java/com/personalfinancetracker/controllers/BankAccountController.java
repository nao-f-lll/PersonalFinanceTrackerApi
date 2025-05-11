package com.personalfinancetracker.controllers;

import com.personalfinancetracker.domain.dto.BankAccountDto;
import com.personalfinancetracker.domain.dto.validation.CreateGroup;
import com.personalfinancetracker.domain.dto.validation.PartialUpdateGroup;
import com.personalfinancetracker.domain.entities.BankAccountEntity;
import com.personalfinancetracker.exceptions.ErrorResponse;
import com.personalfinancetracker.mapper.Mapper;
import com.personalfinancetracker.service.BankAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

import static com.personalfinancetracker.utils.DataSanitizer.sanitize;

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

    @GetMapping(path = "/v1/bank-accounts/{bank-account-id}")
    public ResponseEntity<Object> getBankAccount( @PathVariable("bank-account-id") Long bankAccountId) {

        Optional<BankAccountEntity> saveBankAccount = bankAccountService.findOne(bankAccountId);
        if (saveBankAccount.isPresent()) {
            BankAccountDto response = bankAccountMapper.mapTo(saveBankAccount.get());
            response.setUserDto(sanitize(response.getUserDto()));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else {
            ErrorResponse errorResponse = ErrorResponse
                    .builder()
                    .message("Bank Account Does Not Exists")
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/v1/bank-accounts/user/{user-id}")
    public Page<BankAccountDto> getBankAccounts(Pageable pageable, @PathVariable("user-id") Long userId) {
        Page<BankAccountEntity> bankAccountEntities = bankAccountService.findAllByUserId(pageable, userId);
        Page<BankAccountDto> bankAccountDtos = bankAccountEntities.map(bankAccountMapper::mapTo);
        return bankAccountDtos.map(bankAccountDto -> {
            bankAccountDto.setUserDto(sanitize(bankAccountDto.getUserDto()));
            return bankAccountDto;
        });
    }

    @PostMapping(path = "/v1/bank-accounts")
    public ResponseEntity<Object> createBankAccount(@Validated(CreateGroup.class) @RequestBody BankAccountDto bankAccountDto) {
        try {
            BankAccountEntity bankAccountEntity = bankAccountMapper.mapFrom(bankAccountDto);
            BankAccountEntity saveBankAccount = bankAccountService.create(bankAccountEntity);
            if (saveBankAccount != null) {
                BankAccountDto response = bankAccountMapper.mapTo(saveBankAccount);
                response.setUserDto(sanitize(response.getUserDto()));
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("The user does not exists",HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(path = "/v1/bank-accounts/{bank-account-id}")
    public ResponseEntity<Object> partialUpdate(@Validated(PartialUpdateGroup.class) @RequestBody BankAccountDto bankAccountDto,
                                                @PathVariable("bank-account-id") Long bankAccountId) {
        ResponseEntity<Object> errorResponse = doesBankAccountExists(bankAccountId);
        if (errorResponse != null) return errorResponse;
        bankAccountService.fillBankAccountDtoWithDetails(bankAccountDto, bankAccountId);
        try {
            BankAccountEntity bankAccountEntity = bankAccountMapper.mapFrom(bankAccountDto);
            BankAccountEntity saveBankAccount = bankAccountService.partialUpdate(bankAccountEntity);
            BankAccountDto response = bankAccountMapper.mapTo(saveBankAccount);
            response.setUserDto(sanitize(response.getUserDto()));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path ="/v1/bank-accounts/{bank-account-id}")
    public ResponseEntity<Object> deleteBankAccounts(@PathVariable("bank-account-id") Long bankAccountId) {
        if (doesBankAccountExists(bankAccountId) == null) {
            Optional<BankAccountEntity> bankAccountEntity = bankAccountService.findOne(bankAccountId);
            if (bankAccountEntity.isPresent()) {
                bankAccountService.delete(bankAccountId, bankAccountEntity.get().getUserEntity().getId());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<Object> doesBankAccountExists(Long bankAccountId) {
        if (!bankAccountService.isExists(bankAccountId)) {
            ErrorResponse errorResponse = ErrorResponse
                    .builder()
                    .message("Bank Account Does Not Exists")
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        return null;
    }
}