package com.personalfinancetracker.utils;

import com.personalfinancetracker.domain.dto.BankAccountDto;
import com.personalfinancetracker.domain.dto.UserDto;


public class DataSanitizer {
    public static UserDto sanitize(UserDto userDto) {
        if (userDto != null) {
            userDto.setUpdate_date(null);
            userDto.setCreation_date(null);
            userDto.setName(null);
            userDto.setEmail(null);
            userDto.setPassword(null);
            return userDto;
        }
        return null;
    }

    public static BankAccountDto sanitize(BankAccountDto bankAccountDto) {
        if (bankAccountDto != null) {
            bankAccountDto.setUserDto(null);
            bankAccountDto.setType(null);
            bankAccountDto.setName(null);
            return bankAccountDto;
        }
        return null;
    }
}