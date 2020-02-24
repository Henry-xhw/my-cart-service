package com.active.services.cart.domain;

import com.active.services.order.discount.OrderLineDiscountOrigin;
import com.active.services.product.AmountType;
import com.active.services.product.DiscountAlgorithm;
import com.active.services.product.DiscountType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor
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

    private Boolean applyToRecurringBilling;
    private OrderLineDiscountOrigin origin = OrderLineDiscountOrigin.AUTOMATIC;
    private Long discountGroupId;

    public Discount(String name, String description, @NonNull BigDecimal amount, @NonNull AmountType amountType) {
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.amountType = amountType;
    }

    public Discount(String name, String description, @NonNull BigDecimal amount, @NonNull AmountType amountType,
                    Long discountId, DiscountType discountType, String couponCode, DiscountAlgorithm algorithm,
                    Boolean applyToRecurringBilling, UUID identifier, Long cartId) {
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.amountType = amountType;
        this.discountId = discountId;
        this.discountType = discountType;
        this.couponCode = couponCode;
        this.algorithm = algorithm;
        this.applyToRecurringBilling = applyToRecurringBilling;
        this.cartId = cartId;
        this.setIdentifier(identifier);
    }
}
