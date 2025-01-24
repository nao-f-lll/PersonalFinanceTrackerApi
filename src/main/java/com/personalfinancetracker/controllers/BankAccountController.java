package com.personalfinancetracker.controllers;

import com.personalfinancetracker.domain.dto.BankAccountDto;
import com.personalfinancetracker.domain.entities.BankAccountEntity;
import com.personalfinancetracker.mapper.Mapper;
import com.personalfinancetracker.service.BankAccountService;
import com.personalfinancetracker.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BankAccountController {

    private static final Logger logger = LoggerFactory.getLogger(BankAccountController.class);
    private final BankAccountService bankAccountService;
    private final Mapper<BankAccountEntity, BankAccountDto> bankAccountMapper;



    @Autowired
    public BankAccountController(Mapper<BankAccountEntity, BankAccountDto> bankAccountMapper,  BankAccountService bankAccountService, UserService userService) {
        this.bankAccountMapper = bankAccountMapper;
        this.bankAccountService = bankAccountService;
    }

    @GetMapping(path = "/v1/bank-accounts/{user-id}")
    public List<BankAccountDto> getBankAccounts(@PathVariable("user-id") Long userId) {
        List<BankAccountEntity> authors = bankAccountService.findAllByUserId(userId);
        return authors.stream().map(bankAccountMapper::mapTo).collect(Collectors.toList());
    }

    @PostMapping(path = "/v1/bank-accounts")
    public ResponseEntity<BankAccountDto> createBankAccount(@Valid @RequestBody BankAccountDto bankAccountDto) {
        try {
            BankAccountEntity bankAccountEntity = bankAccountMapper.mapFrom(bankAccountDto);
            BankAccountEntity saveBankAccount = bankAccountService.create(bankAccountEntity);
            BankAccountDto response = bankAccountMapper.mapTo(saveBankAccount);
            if (response != null) {
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}