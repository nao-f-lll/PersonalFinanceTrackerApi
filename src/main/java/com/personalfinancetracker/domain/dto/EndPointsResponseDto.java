package com.personalfinancetracker.domain.dto;

import com.personalfinancetracker.enums.StatusMessages;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EndPointsResponseDto<T> {

    private T object;
    private String message;
    private StatusMessages status;

}
