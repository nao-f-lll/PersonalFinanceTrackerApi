package com.personalfinancetracker.enums;

import lombok.Getter;

@Getter
public enum StatusMessages {

    PAYMENT_ERROR("The payment type is not compatible with having a bank account"),
    AMOUNT_ERROR("The transaction amount is bigger than the balance that you have"),
    PARENT_CATEGORY_ERROR("Parent category are not acceptable"),
    USER_NOT_FOUND_ERROR("The user with this id does not exist"),
    BANK_ACCOUNT_NOT_FOUND_ERROR("the bank account with this id does not exists"),
    SUCCESS("The operation has been correct"),
    BANK_ACCOUNT_HAS_DIFFERENT_USER_ID("The user does not own the bank account provided"),
    NO_TRANSACTION_HAS_THIS_ID("There's no transaction with the given ID"),
    CATEGORY_DOES_NOT_EXISTS("The given category does not exists"),
    AMOUNT_IS_NULL("Can't assign a null amount");
    private final String description;

    StatusMessages(String description) {
        this.description = description;
    }
}