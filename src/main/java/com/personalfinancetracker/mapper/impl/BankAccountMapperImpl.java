package com.personalfinancetracker.mapper.impl;

import com.personalfinancetracker.domain.dto.BankAccountDto;
import com.personalfinancetracker.domain.entities.BankAccountEntity;
import com.personalfinancetracker.enums.AccountBankType;
import com.personalfinancetracker.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BankAccountMapperImpl  implements Mapper<BankAccountEntity, BankAccountDto> {

    private final ModelMapper modelMapper;

    public BankAccountMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public BankAccountDto mapTo(BankAccountEntity bankAccountEntity) {
        BankAccountDto bankAccountDto = modelMapper.map(bankAccountEntity, BankAccountDto.class);
        bankAccountDto.setType(bankAccountEntity.getType().name());
        return bankAccountDto;
    }

    @Override
    public BankAccountEntity mapFrom(BankAccountDto bankAccountDto) {
        BankAccountEntity bankAccountEntity =  modelMapper.map(bankAccountDto, BankAccountEntity.class);
        bankAccountEntity.setType(AccountBankType.valueOf(bankAccountDto.getType()));
        return bankAccountEntity;
    }
}
