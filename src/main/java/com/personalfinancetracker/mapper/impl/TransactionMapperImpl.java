package com.personalfinancetracker.mapper.impl;

import com.personalfinancetracker.domain.dto.TransactionDto;
import com.personalfinancetracker.domain.entities.TransactionEntity;
import com.personalfinancetracker.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapperImpl implements Mapper<TransactionEntity, TransactionDto> {

    private final ModelMapper modelMapper;

    public TransactionMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public TransactionDto mapTo(TransactionEntity transactionEntity) {
        return modelMapper.map(transactionEntity, TransactionDto.class);
    }

    @Override
    public TransactionEntity mapFrom(TransactionDto transactionDto) {
        return modelMapper.map(transactionDto, TransactionEntity.class);
    }
}
