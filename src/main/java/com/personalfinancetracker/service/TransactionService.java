package com.personalfinancetracker.service;

import com.personalfinancetracker.domain.dto.EndPointsResponseDto;
import com.personalfinancetracker.domain.entities.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;


@Component
public interface TransactionService {
    EndPointsResponseDto<TransactionEntity> create(TransactionEntity transactionEntity);

//    Page<TransactionEntity> findAll(Long userId, Pageable pageable);
//
    Page<TransactionEntity> findAll(Long userId, Pageable pageable);

    boolean isExists(Long transactionId);

    void delete(Long transactionId);
//
//    EndPointsResponseDto<TransactionEntity> partialUpdate(TransactionEntity transactionEntity);
}
