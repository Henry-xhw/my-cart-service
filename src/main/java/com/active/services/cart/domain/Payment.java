package com.active.services.cart.domain;

import com.active.services.DomainObject;
import com.active.services.TenderType;
import com.active.services.payment.management.PaymentStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class Payment extends DomainObject {

    private UUID identifier;

    private TenderType tenderType;

    private PaymentStatus paymentStatus;

    private BigDecimal amount;
}
