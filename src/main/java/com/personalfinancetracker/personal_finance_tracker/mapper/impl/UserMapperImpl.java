package com.personalfinancetracker.personal_finance_tracker.mapper.impl;

import com.personalfinancetracker.personal_finance_tracker.domain.dto.UserDto;
import com.personalfinancetracker.personal_finance_tracker.domain.entities.UserEntity;
import com.personalfinancetracker.personal_finance_tracker.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements Mapper<UserEntity, UserDto> {

    private final ModelMapper modelMapper;

    public UserMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDto mapTo(UserEntity userEntity) {
        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserEntity mapFrom(UserDto userDto) {
        return modelMapper.map(userDto, UserEntity.class);
    }
}
