package com.active.services.cart.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentAccountResult {
    private PaymentType paymentType;
    private String lastDigitsAccountNumber;
    private BigDecimal amount;
    private EcheckPaymentTenderType tenderType;

}
