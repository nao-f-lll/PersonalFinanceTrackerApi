package com.personalfinancetracker.service.impl;

import com.personalfinancetracker.domain.dto.EndPointsResponseDto;
import com.personalfinancetracker.domain.entities.BankAccountEntity;
import com.personalfinancetracker.domain.entities.CategoryEntity;
import com.personalfinancetracker.domain.entities.TransactionEntity;
import com.personalfinancetracker.domain.entities.UserEntity;
import com.personalfinancetracker.enums.PaymentMethod;
import com.personalfinancetracker.enums.StatusMessages;
import com.personalfinancetracker.event.TransactionCreateUpdateEvent;
import com.personalfinancetracker.repositories.TransactionRepository;
import com.personalfinancetracker.service.BankAccountService;
import com.personalfinancetracker.service.CategoryService;
import com.personalfinancetracker.service.TransactionService;
import com.personalfinancetracker.service.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

import static com.personalfinancetracker.enums.StatusMessages.*;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final BankAccountService bankAccountService;
    private final CategoryService categoryService;
    private final TransactionRepository transactionRepository;
    private final ApplicationEventPublisher eventPublisher;

    public TransactionServiceImpl(BankAccountService bankAccountService, CategoryService categoryService, TransactionRepository transactionRepository, ApplicationEventPublisher eventPublisher) {
        this.bankAccountService = bankAccountService;
        this.categoryService = categoryService;
        this.transactionRepository = transactionRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public EndPointsResponseDto<TransactionEntity> create(TransactionEntity transactionEntity) {

        EndPointsResponseDto<TransactionEntity> categoryResponse = checkForSubCategory(transactionEntity);
        if (categoryResponse != null) return categoryResponse;

        EndPointsResponseDto<TransactionEntity> bankAccountResponse = checkBankAccount(transactionEntity);
        if (bankAccountResponse != null) return bankAccountResponse;

        EndPointsResponseDto<TransactionEntity> response = new EndPointsResponseDto<>();
        response.setObject(transactionRepository.save(transactionEntity));
        response.setStatus(SUCCESS);
        return response;
    }


    private EndPointsResponseDto<TransactionEntity> checkForSubCategory(TransactionEntity transactionEntity) {
        Optional<CategoryEntity> categoryEntity = categoryService.findOne(transactionEntity.getCategoryEntity().getId());
        if (categoryEntity.isPresent()) {
            if (categoryEntity.get().getParentCategory() != null) {
                transactionEntity.setCategoryEntity(categoryEntity.get());
            } else {
                return createResponse(PARENT_CATEGORY_ERROR);
            }
        }
        return null;
    }

    private EndPointsResponseDto<TransactionEntity> checkBankAccount(TransactionEntity transactionEntity) {
        Optional<BankAccountEntity> bankAccountEntity = bankAccountService.findOne(transactionEntity.getBankAccountEntity().getId());
        if (bankAccountEntity.isPresent()) {
            if (transactionEntity.getAmount().compareTo(bankAccountEntity.get().getBalance()) > 0 ) {
                return createResponse(AMOUNT_ERROR);
            }
            transactionEntity.setBankAccountEntity(bankAccountEntity.get());
            transactionEntity.setUserEntity(bankAccountEntity.get().getUserEntity());
            TransactionCreateUpdateEvent event = new TransactionCreateUpdateEvent(this, transactionEntity.getBankAccountEntity().getId(), transactionEntity.getAmount(), false);
            eventPublisher.publishEvent(event);
        } else {
            return createResponse(BANK_ACCOUNT_NOT_FOUND_ERROR);
        }
        return null;
    }

    private EndPointsResponseDto<TransactionEntity> createResponse(StatusMessages statusMessages) {
        EndPointsResponseDto<TransactionEntity> response = new EndPointsResponseDto<>();
        response.setMessage(statusMessages.getDescription());
        response.setStatus(statusMessages);
        return response;
    }
//
//
//    private EndPointsResponseDto<TransactionEntity> createResponse(TransactionEntity transaction, StatusMessages statusMessages) {
//        EndPointsResponseDto<TransactionEntity> response = new EndPointsResponseDto<>();
//        response.setMessage(statusMessages.getDescription());
//        response.setStatus(statusMessages);
//        response.setObject(transaction);
//        return response;
//    }
//
//
    @Override
    public Page<TransactionEntity> findAll(Long userId, Pageable pageable) {
        return transactionRepository.findAllByUserId(userId, pageable);
    }
//
    @Override
    public boolean isExists(Long transactionId) {
        return transactionRepository.existsById(transactionId);
    }

    @Override
    public void delete(Long transactionId) {
        handleDeletionForOne(transactionId);
        transactionRepository.deleteById(transactionId);
    }

////    @Override
////    public EndPointsResponseDto<TransactionEntity> partialUpdate(TransactionEntity transactionEntity) {
////        return transactionRepository.findById(transactionEntity.getId()).map(savedTransaction -> {
////
////            EndPointsResponseDto<TransactionEntity> categoryResponse = checkCategory(transactionEntity, savedTransaction);
////            if (categoryResponse != null) return categoryResponse;
////
////            if (transactionEntity.getPaymentMethod() == PaymentMethod.CASH && savedTransaction.getPaymentMethod() != PaymentMethod.CASH) {
////                sendEvent(savedTransaction.getBankAccountEntity().getId(), savedTransaction.getAmount(), true);
////                savedTransaction.setBankAccountEntity(null);
////                savedTransaction.setPaymentMethod(transactionEntity.getPaymentMethod());
////            } else if (transactionEntity.getPaymentMethod() != PaymentMethod.CASH && savedTransaction.getPaymentMethod() == PaymentMethod.CASH) {
////                EndPointsResponseDto<TransactionEntity> bankAccountResponse = checkForUpdatingBankAccount(transactionEntity, savedTransaction);
////                if (bankAccountResponse != null) return bankAccountResponse;
////            }
////            if(transactionEntity.getPaymentMethod() == PaymentMethod.CASH && savedTransaction.getPaymentMethod() == PaymentMethod.CASH) {
////                savedTransaction.setPaymentMethod(transactionEntity.getPaymentMethod());
////            } else if (transactionEntity.getPaymentMethod() != PaymentMethod.CASH && savedTransaction.getPaymentMethod() != PaymentMethod.CASH) {
////                EndPointsResponseDto<TransactionEntity> bankAccountResponse = checkForUpdatingBankAccount1(transactionEntity, savedTransaction);
////                if (bankAccountResponse != null) return bankAccountResponse;
////            }
////            EndPointsResponseDto<TransactionEntity> userResponse = checkUser(savedTransaction);
////            if (userResponse != null) return userResponse;
////            return createResponse(transactionRepository.save(savedTransaction), SUCCESS);
////        }).orElseThrow(() -> new RuntimeException("The Transaction Does Not Exist"));
////    }
//
//    private EndPointsResponseDto<TransactionEntity> checkForUpdatingBankAccount1(TransactionEntity transactionEntity, TransactionEntity savedTransaction) {
//        if (transactionEntity.getBankAccountEntity() != null ) {
//            Optional<BankAccountEntity> bankAccountEntity = bankAccountService.findOne(transactionEntity.getBankAccountEntity().getId());
//            if (bankAccountEntity.isPresent()) {
//                if (transactionEntity.getAmount() != null) {
//                    sendEvent(savedTransaction.getBankAccountEntity().getId(), savedTransaction.getAmount(), true);
//                    sendEvent(transactionEntity.getBankAccountEntity().getId(), transactionEntity.getAmount(), false);
//                    savedTransaction.setBankAccountEntity(bankAccountEntity.get());
//                    savedTransaction.setPaymentMethod(transactionEntity.getPaymentMethod());
//                } else {
//                    return createResponse(AMOUNT_IS_NULL);
//                }
//            } else {
//                return createResponse(BANK_ACCOUNT_NOT_FOUND_ERROR);
//            }
//        } else {
//            return createResponse(BANK_ACCOUNT_NOT_FOUND_ERROR);
//        }
//        return null;
//    }
//
//    private EndPointsResponseDto<TransactionEntity> checkForUpdatingBankAccount(TransactionEntity transactionEntity, TransactionEntity savedTransaction) {
//        if (transactionEntity.getBankAccountEntity() != null ) {
//            Optional<BankAccountEntity> bankAccountEntity = bankAccountService.findOne(transactionEntity.getBankAccountEntity().getId());
//            if (bankAccountEntity.isPresent()) {
//                savedTransaction.setBankAccountEntity(bankAccountEntity.get());
//                if (transactionEntity.getAmount() != null) {
//                    sendEvent(savedTransaction.getBankAccountEntity().getId(), transactionEntity.getAmount(), false);
//                    savedTransaction.setPaymentMethod(transactionEntity.getPaymentMethod());
//                } else {
//                    return createResponse(AMOUNT_IS_NULL);
//                }
//            } else {
//                return createResponse(BANK_ACCOUNT_NOT_FOUND_ERROR);
//            }
//        } else {
//            return createResponse(BANK_ACCOUNT_NOT_FOUND_ERROR);
//        }
//        return null;
//    }
//
//    private EndPointsResponseDto<TransactionEntity> checkCategory(TransactionEntity transactionEntity, TransactionEntity savedTransaction) {
//        Optional<CategoryEntity> categoryEntity = categoryService.findOne(transactionEntity.getCategoryEntity().getId());
//        if (categoryEntity.isPresent()) {
//            savedTransaction.setCategoryEntity(categoryEntity.get());
//        } else {
//            return createResponse(CATEGORY_DOES_NOT_EXISTS);
//        }
//        return null;
//    }
//
//    private void sendEvent(Long transactionId, BigDecimal amount, Boolean isAddition) {
//        TransactionCreateUpdateEvent event = new TransactionCreateUpdateEvent(this, transactionId, amount, isAddition);
//        eventPublisher.publishEvent(event);
//    }
//
//
    private void handleDeletionForOne(Long transactionId) {
        Optional<TransactionEntity> transactionEntity = transactionRepository.findById(transactionId);
        if (transactionEntity.isPresent()) {
            TransactionCreateUpdateEvent event = new TransactionCreateUpdateEvent(this, transactionEntity.get().getBankAccountEntity().getId(), transactionEntity.get().getAmount(), true);
            eventPublisher.publishEvent(event);
        }
    }
}