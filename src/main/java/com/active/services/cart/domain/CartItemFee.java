package com.active.services.cart.domain;

import com.active.services.cart.model.AmountType;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.model.DiscountOrigin;
import com.active.services.cart.model.DiscountType;
import com.active.services.cart.model.FeeTransactionType;
import com.active.services.order.discount.OrderLineDiscountOrigin;
import com.active.services.product.nextgen.v1.dto.fee.FeeDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
public class CartItemFee extends BaseTree<CartItemFee> {
    private String name;

    private String description;

    private CartItemFeeType type;

    private FeeTransactionType transactionType;

    private Integer units;

    private BigDecimal unitPrice;

    private DiscountType discountType;

    private Boolean applyToRecurringBilling = false;

    private Long discountId;

    private UUID keyerUUID;

    private Long discountGroupId;

    private AmountType amountType;

    private BigDecimal amount;

    private DiscountOrigin origin;

    private Boolean hasSameDiscountId;

    private boolean isCancelled;

    public boolean isCarryOverDiscount() {
        return origin.equals(OrderLineDiscountOrigin.CARRY_OVER);
    }

    public static CartItemFee buildCartItemFee(CartItem cartItem, CartItemFeeType cartItemFeeType) {
        CartItemFee unitPriceFee = new CartItemFee();
        unitPriceFee.setIdentifier(UUID.randomUUID());
        unitPriceFee.setDescription(cartItem.getProductDescription());
        unitPriceFee.setName(cartItem.getProductName());
        unitPriceFee.setTransactionType(FeeTransactionType.DEBIT);
        unitPriceFee.setType(cartItemFeeType);
        unitPriceFee.setUnitPrice(cartItem.getUnitPrice());
        unitPriceFee.setUnits(cartItem.getQuantity());
        return unitPriceFee;
    }

    public static CartItemFee buildCartItemFee(CartItem cartItem, FeeDto feeDto,
                                               CartItemFeeType cartItemFeeType) {
        CartItemFee unitPriceFee = new CartItemFee();
        unitPriceFee.setIdentifier(UUID.randomUUID());
        unitPriceFee.setDescription(feeDto.getDescription());
        unitPriceFee.setName(feeDto.getName());
        unitPriceFee.setTransactionType(FeeTransactionType.DEBIT);
        unitPriceFee.setType(cartItemFeeType);
        unitPriceFee.setUnitPrice(feeDto.getAmount());
        unitPriceFee.setUnits(cartItem.getQuantity());
        return unitPriceFee;
    }
}
