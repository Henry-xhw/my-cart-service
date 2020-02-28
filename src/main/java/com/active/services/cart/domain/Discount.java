package com.active.services.cart.domain;

import com.active.services.order.discount.OrderLineDiscountOrigin;
import com.active.services.product.AmountType;
import com.active.services.product.DiscountAlgorithm;
import com.active.services.product.DiscountType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@NoArgsConstructor
@Setter
@SuperBuilder
public class Discount extends BaseDomainObject {

    private Long cartId;
    private String name;
    private String description;
    private BigDecimal amount;
    private AmountType amountType;
    private Long discountId;
    private DiscountType discountType;
    private String couponCode;
    private DiscountAlgorithm algorithm;

    private Boolean applyToRecurringBilling;
    private OrderLineDiscountOrigin origin = OrderLineDiscountOrigin.AUTOMATIC;
    private Long discountGroupId;
    private Instant startDate;
    private Instant endDate;

}
