package com.active.services.cart.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PaymentAccount {
    @NotNull
    private String amsAccountId;
    @NotNull
    private PaymentType paymentType;
}
