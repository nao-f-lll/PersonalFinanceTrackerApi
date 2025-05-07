package com.personalfinancetracker.controllers;

import com.personalfinancetracker.domain.dto.EndPointsResponseDto;
import com.personalfinancetracker.domain.dto.TransactionDto;
import com.personalfinancetracker.domain.dto.validation.CreateGroup;
import com.personalfinancetracker.domain.dto.validation.PartialUpdateGroup;
import com.personalfinancetracker.domain.entities.TransactionEntity;
import com.personalfinancetracker.enums.StatusMessages;
import com.personalfinancetracker.mapper.Mapper;
import com.personalfinancetracker.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.personalfinancetracker.utils.DataSanitizer.sanitize;

@RestController
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
    private final Mapper<TransactionEntity, TransactionDto> transactionMapper;
    private final TransactionService transactionService;

    public TransactionController(Mapper<TransactionEntity, TransactionDto> transactionMapper, TransactionService transactionService) {
        this.transactionMapper = transactionMapper;
        this.transactionService = transactionService;
    }

//    @GetMapping("/v1/transactions/{user-id}")
//    public Page<TransactionDto> getTransactions(@PathVariable("user-id") Long userId, Pageable pageable) {
//        Page<TransactionEntity> transactionEntities = transactionService.findAll(userId ,pageable);
//        Page<TransactionDto> transactionDtos = transactionEntities.map(transactionMapper::mapTo);
//
//        return transactionDtos.map(transactionDto -> {
//            transactionDto.setUserDto(sanitize(transactionDto.getUserDto()));
//            transactionDto.setBankAccountDto(sanitize(transactionDto.getBankAccountDto()));
//            return transactionDto;
//        });
//    }

    @PostMapping(path = "/v1/transactions")
    public ResponseEntity<Object> createTransaction(@Validated({CreateGroup.class}) @RequestBody TransactionDto transactionDto) {
        try {
            TransactionEntity transactionEntity = transactionMapper.mapFrom(transactionDto);
            EndPointsResponseDto<TransactionEntity> savedTransaction = transactionService.create(transactionEntity);
            StatusMessages returnedStatus = savedTransaction.getStatus();
            return mapStatusToResponse(returnedStatus, savedTransaction);
        } catch(Exception e){
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<Object> mapStatusToResponse(StatusMessages returnedStatus, EndPointsResponseDto<TransactionEntity> savedTransaction) {
        switch (returnedStatus) {
            case PAYMENT_ERROR, AMOUNT_ERROR, PARENT_CATEGORY_ERROR, BANK_ACCOUNT_HAS_DIFFERENT_USER_ID:
                return new ResponseEntity<>(savedTransaction, HttpStatus.BAD_REQUEST);
            case USER_NOT_FOUND_ERROR,BANK_ACCOUNT_NOT_FOUND_ERROR:
                return new ResponseEntity<>(savedTransaction, HttpStatus.NOT_FOUND);
            default:
                TransactionDto response = transactionMapper.mapTo(savedTransaction.getObject());
                response.setUserDto(sanitize(response.getUserDto()));
                response.setBankAccountDto(sanitize(response.getBankAccountDto()));
                return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
    }

//    @DeleteMapping(path = "/v1/users/{user-id}/bank-accounts/{bank-account-id}/transactions/{transaction-id}")
//    public ResponseEntity<TransactionDto> deleteTransaction(@PathVariable("transaction-id") Long transactionId) {
//        if (transactionService.isExists(transactionId)) {
//            transactionService.delete(transactionId);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }

//
//    @PatchMapping(path = "/v1/transactions/{transaction-id}")
//    public ResponseEntity<Object> partialUpdateTransaction(@PathVariable("transaction-id") Long transactionId, @Validated(PartialUpdateGroup.class) @RequestBody TransactionDto transactionDto) {
//        if (!transactionService.isExists(transactionId)) {
//            EndPointsResponseDto<TransactionEntity> response = new EndPointsResponseDto<>();
//            response.setMessage("There's no transaction with the given ID");
//            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//        } else {
//            try {
//                transactionDto.setId(transactionId);
//                TransactionEntity transactionEntity = transactionMapper.mapFrom(transactionDto);
//                EndPointsResponseDto<TransactionEntity> response = transactionService.partialUpdate(transactionEntity);
//                response.getObject().setUserEntity(null);
//                response.getObject().setBankAccountEntity(null);
//                return new ResponseEntity<>(response, HttpStatus.OK);
//            } catch (Exception e) {
//                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//            }
//        }
//    }
}