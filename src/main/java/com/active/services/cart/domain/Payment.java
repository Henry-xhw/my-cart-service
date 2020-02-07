package com.active.services.cart.domain;

import com.active.services.DomainObject;
import com.active.services.TenderType;
import com.active.services.payment.management.PaymentStatus;


import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class Payment extends DomainObject {

    private UUID identifier;

    private TenderType tenderType;

    private PaymentStatus paymentStatus;

    private BigDecimal amount;
}
