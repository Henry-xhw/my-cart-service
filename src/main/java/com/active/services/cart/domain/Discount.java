package com.active.services.cart.domain;

import com.active.services.order.discount.OrderLineDiscountOrigin;
import com.active.services.product.AmountType;
import com.active.services.product.DiscountAlgorithm;
import com.active.services.product.DiscountType;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
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

    private boolean applyToRecurringBilling;
    private OrderLineDiscountOrigin origin;
    private Long discountGroupId;
    private UUID keyerUUID;
    private Instant startDate;
    private Instant endDate;
    private Long membershipId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Discount discount = (Discount) o;
        return Objects.equals(discountId, discount.discountId) &&
                discountType == discount.discountType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(discountId, discountType);
    }
}
