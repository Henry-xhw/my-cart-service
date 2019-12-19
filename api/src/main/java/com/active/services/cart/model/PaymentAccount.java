package com.active.services.cart.model;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PaymentAccount {
    @NotNull
    private String amsAccountId;
    @NotNull
    private PaymentType paymentType;
}
