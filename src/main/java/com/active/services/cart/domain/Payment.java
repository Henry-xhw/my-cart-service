package com.active.services.cart.domain;

import com.active.services.DomainObject;
import com.active.services.TenderType;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Payment extends DomainObject {

    private TenderType tenderType;

    private PaymentStatus paymentStatus;

    private BigDecimal amount;
}
