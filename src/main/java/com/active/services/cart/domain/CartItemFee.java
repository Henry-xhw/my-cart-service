package com.active.services.cart.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.active.services.cart.model.CartItemFeeTransactionType;
import com.active.services.cart.model.CartItemFeeType;

import lombok.Data;

@Data
public class CartItemFee extends BaseDomainObject {
    private Long parentId;

    private String name;

    private String description;

    private CartItemFeeType type;

    private CartItemFeeTransactionType transactionType;

    private Integer units;

    private BigDecimal unitPrice;

    private List<CartItemFee> derivedFees = new ArrayList<>();

    public static CartItemFee buildCartItemFee(CartItem cartItem, CartItemFeeType cartItemFeeType) {
        CartItemFee unitPriceFee = new CartItemFee();
        unitPriceFee.setIdentifier(UUID.randomUUID());
        unitPriceFee.setDescription(cartItem.getProductDescription());
        unitPriceFee.setName(cartItem.getProductName());
        unitPriceFee.setTransactionType(CartItemFeeTransactionType.DEBIT);
        unitPriceFee.setType(cartItemFeeType);
        unitPriceFee.setUnitPrice(cartItem.getUnitPrice());
        unitPriceFee.setUnits(cartItem.getQuantity());
        return unitPriceFee;
    }
}
