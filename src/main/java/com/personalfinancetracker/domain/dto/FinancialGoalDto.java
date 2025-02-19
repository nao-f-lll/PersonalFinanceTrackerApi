package com.personalfinancetracker.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.personalfinancetracker.domain.dto.validation.CreateGroup;
import com.personalfinancetracker.domain.dto.validation.PartialUpdateGroup;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialGoalDto {

    private Long id;

    @NotNull(message = "user id cannot be null", groups = CreateGroup.class)
    private UserDto userDto;

    @NotNull(message = "name cannot be null", groups = {CreateGroup.class, PartialUpdateGroup.class})
    private String name;

    @NotNull(message = "targetAmount cannot be null", groups = {CreateGroup.class, PartialUpdateGroup.class})
    private BigDecimal targetAmount;

    private BigDecimal currentAmount;

    @NotNull(message = "expectedDate cannot be null", groups = {CreateGroup.class, PartialUpdateGroup.class})
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate expectedDate;
}