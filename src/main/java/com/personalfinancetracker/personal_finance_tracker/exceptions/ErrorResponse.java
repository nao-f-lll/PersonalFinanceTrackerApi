package com.personalfinancetracker.personal_finance_tracker.exceptions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

    private String message;
}
