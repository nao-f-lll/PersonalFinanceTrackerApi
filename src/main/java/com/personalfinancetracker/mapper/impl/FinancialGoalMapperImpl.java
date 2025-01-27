package com.personalfinancetracker.mapper.impl;

import com.personalfinancetracker.domain.dto.FinancialGoalDto;
import com.personalfinancetracker.domain.entities.FinancialGoalEntity;
import com.personalfinancetracker.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class FinancialGoalMapperImpl implements Mapper<FinancialGoalEntity, FinancialGoalDto> {

    private final ModelMapper modelMapper;

    public FinancialGoalMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public FinancialGoalDto mapTo(FinancialGoalEntity financialGoalEntity) {
        return modelMapper.map(financialGoalEntity, FinancialGoalDto.class);
    }

    @Override
    public FinancialGoalEntity mapFrom(FinancialGoalDto financialGoalDto) {
        return modelMapper.map(financialGoalDto, FinancialGoalEntity.class);
    }
}