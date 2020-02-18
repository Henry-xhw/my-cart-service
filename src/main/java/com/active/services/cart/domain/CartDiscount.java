package com.active.services.cart.domain;

import com.active.services.cart.model.AmountType;
import com.active.services.cart.model.DiscountOrigin;
import com.active.services.cart.model.DiscountType;
import com.active.services.order.discount.OrderLineDiscountOrigin;

import java.math.BigDecimal;
import java.util.UUID;

public class CartDiscount extends BaseDomainObject {
    private Long cartId;

    private DiscountType cartDiscountType;

    private Boolean applyToRecurringBilling = false;

    private Long discountId;

    private UUID keyerUUID;

    private Long discountGroupId;

    private AmountType amountType;

    private BigDecimal amount;

    private DiscountOrigin origin;

    public boolean isCarryOverDiscount() {
        return origin.equals(DiscountOrigin.CARRY_OVER);
    }
}
