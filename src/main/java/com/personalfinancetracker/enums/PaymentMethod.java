package com.personalfinancetracker.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    DEBIT_CARD("Tarjeta de Débito"),
    BANK_TRANSFER("Transferencia Bancaria"),
    PAYPAL("PayPal"),
    MOBILE_NFC("Pago Móvil (NFC)"),
    VIRTUAL_CARD("Tarjeta Virtual"),
    DIRECT_DEBIT("Domiciliación de Pago");

    private final String description;

    PaymentMethod(String description) {
        this.description = description;
    }
}