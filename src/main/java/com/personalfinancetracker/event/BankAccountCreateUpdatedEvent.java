package com.personalfinancetracker.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class BankAccountCreateUpdatedEvent extends ApplicationEvent {

    private final Long userID;

    public BankAccountCreateUpdatedEvent(Object source, Long userID) {
        super(source);
        this.userID = userID;
    }
}
