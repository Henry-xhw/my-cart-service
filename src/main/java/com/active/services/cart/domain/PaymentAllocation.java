package com.active.services.cart.domain;

import com.active.services.DomainObject;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentAllocation extends DomainObject {

    private Payment payment;

    private BigDecimal amount;
}
