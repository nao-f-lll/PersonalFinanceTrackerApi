package com.personalfinancetracker.domain.dto;

import com.personalfinancetracker.domain.dto.validation.CreateGroup;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialGoalDto {

    private Long id;

    @NotNull(message = "user id cannot be null", groups = CreateGroup.class)
    private UserDto userDto;

    @NotNull(message = "name cannot be null", groups = CreateGroup.class)
    private String name;

    @NotNull(message = "targetAmount cannot be null", groups = CreateGroup.class)
    private Double targetAmount;

    private Double currentAmount;

    @NotNull(message = "expectedDate cannot be null", groups = CreateGroup.class)
    private Date expectedDate;
}