package com.personalfinancetracker.utils;

import com.personalfinancetracker.domain.dto.BankAccountDto;
import com.personalfinancetracker.domain.dto.UserDto;


public class DataSanitizer {
    public static UserDto sanitize(UserDto userDto) {
        userDto.setUpdate_date(null);
        userDto.setCreation_date(null);
        userDto.setName(null);
        userDto.setEmail(null);
        userDto.setPassword(null);
        return userDto;
    }

    public static BankAccountDto sanitize(BankAccountDto bankAccountDto) {
        bankAccountDto.setUserDto(null);
        // TODO know why the bank account type is not the correct one
        bankAccountDto.setType(null);
        bankAccountDto.setName(null);
        return bankAccountDto;
    }
}
