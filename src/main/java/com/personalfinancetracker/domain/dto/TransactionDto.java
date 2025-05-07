package com.personalfinancetracker.domain.dto;

import com.personalfinancetracker.domain.dto.validation.CreateGroup;
import com.personalfinancetracker.domain.dto.validation.PartialUpdateGroup;
import com.personalfinancetracker.enums.PaymentMethod;
import com.personalfinancetracker.enums.validation.ValueOfEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDto {

    private Long id;

    @NotNull(message = "amount cannot be null", groups = CreateGroup.class)
    private BigDecimal amount;

    private LocalDateTime transactionDate;

    private UserDto userDto;

    @NotNull(message = "category cannot be null", groups = {CreateGroup.class, PartialUpdateGroup.class})
    private CategoryDto categoryDto;

    @NotNull(message = "PaymentMethod cannot be null")
    @ValueOfEnum(enumClass = PaymentMethod.class, message = "Invalid PaymentMethod",
            groups = {CreateGroup.class, PartialUpdateGroup.class})
    private String paymentMethod;

    @NotNull(message = "BankAccount ID cannot be null", groups = CreateGroup.class)
    private BankAccountDto bankAccountDto;
}
