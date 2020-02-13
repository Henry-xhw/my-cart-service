package com.active.services.cart.model.v1;

import com.active.services.cart.model.AmountType;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.model.DiscountOrigin;
import com.active.services.cart.model.DiscountType;
import com.active.services.cart.model.FeeTransactionType;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class CartItemFeeDto extends BaseDto {
    private Long parentId;

    private String name;

    private String description;

    @NotNull
    private CartItemFeeType type;

    @NotNull
    private FeeTransactionType transactionType;

    @NotNull
    @Min(value = 1)
    private Integer units;

    @Min(value = 0)
    @Digits(integer = 17, fraction = 2)
    private BigDecimal unitPrice;

    private DiscountType discountType;

    private Boolean applyToRecurringBilling = false;

    private Long discountId;

    private UUID keyerUUID;

    private Long discountGroupId;

    private AmountType amountType;

    @Min(value = 0)
    @Digits(integer = 17, fraction = 2)
    private BigDecimal amount;

    private DiscountOrigin origin;

    private Boolean hasSameDiscountId;

    private boolean isCancelled;
}
