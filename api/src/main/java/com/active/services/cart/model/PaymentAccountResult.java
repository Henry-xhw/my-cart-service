package com.active.services.cart.model;

import java.math.BigDecimal;
import java.util.UUID;

import com.active.services.CardType;

import lombok.Data;

@Data
public class PaymentAccountResult {
    private UUID paymentId;
    private PaymentType paymentType;
    private String lastDigitsAccountNumber;
    private BigDecimal amount;
    private AuthorizedStatus authorizedStatus;
    //only for CC
    private CardType cardType;


}
