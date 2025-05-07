package com.personalfinancetracker.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;

@Getter
public class TransactionCreateUpdateEvent extends ApplicationEvent {

    private final Long bankAccountId;
    private final BigDecimal amount;
    private final Boolean isAddition;

    public TransactionCreateUpdateEvent(Object source, Long bankAccountId, BigDecimal amount, Boolean isAddition) {
        super(source);
        this.bankAccountId = bankAccountId;
        this.amount = amount;
        this.isAddition = isAddition;
    }
}
