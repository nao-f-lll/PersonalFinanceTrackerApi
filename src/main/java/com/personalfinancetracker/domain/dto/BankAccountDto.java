package com.personalfinancetracker.domain.dto;

import com.personalfinancetracker.enums.AccountBankType;
import com.personalfinancetracker.enums.validation.ValueOfEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccountDto {

    private Long id;

    @NotNull(message = "user id cannot be null")
    private UserDto userDto;

    @NotNull(message = "Account bank type cannot be null")
    @ValueOfEnum(enumClass = AccountBankType.class, message = "Invalid account bank type")
    private String type;

    @NotNull(message = "Account balance type cannot be null")
    private Double balance;

    private String name;
}
