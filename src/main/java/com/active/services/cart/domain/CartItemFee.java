package com.active.services.cart.domain;

import java.math.BigDecimal;
import java.util.UUID;

import com.active.services.cart.model.FeeTransactionType;
import com.active.services.cart.model.CartItemFeeType;
import lombok.Data;

@Data
public class CartItemFee extends BaseTree<CartItemFee> {
    private String name;

    private String description;

    private CartItemFeeType type;

    private FeeTransactionType transactionType;

    private Integer units;

    private BigDecimal unitPrice;

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
}
