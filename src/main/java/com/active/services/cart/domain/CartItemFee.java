package com.active.services.cart.domain;

import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.model.FeeTransactionType;
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

    private BigDecimal dueAmount;

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
