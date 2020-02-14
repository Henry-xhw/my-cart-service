package com.active.services.cart.domain;

import com.active.services.cart.model.AmountType;
import com.active.services.cart.model.DiscountOrigin;
import com.active.services.cart.model.DiscountType;
import com.active.services.order.discount.OrderLineDiscountOrigin;

import java.math.BigDecimal;
import java.util.UUID;

public class Discount extends BaseDomainObject {
    private Long cartItemFeeId;

    private DiscountType discountType;

    private Boolean applyToRecurringBilling = false;

    private Long discountId;

    private UUID keyerUUID;

    private Long discountGroupId;

    private AmountType amountType;

    private BigDecimal amount;

    private DiscountOrigin origin;

    private Boolean hasSameDiscountId;

    public boolean isCarryOverDiscount() {
        return origin.equals(OrderLineDiscountOrigin.CARRY_OVER);
    }
}
