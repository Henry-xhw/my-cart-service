package com.active.services.cart.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PaymentAccountResult {
    private PaymentType paymentType;
    private String lastDigitsAccountNumber;
    private BigDecimal amount;
    private EcheckPaymentTenderType tenderType;

}
