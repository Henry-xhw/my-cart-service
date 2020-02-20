package com.active.services.cart.service.quote;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.model.FeeTransactionType;
import com.active.services.cart.service.quote.discount.domain.Discount;
import com.active.services.product.nextgen.v1.dto.fee.FeeDto;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public class CartItemFeeBuilder {

    public static CartItemFee buildOverridePriceItemFee(CartItem cartItem) {
        return buildCartItemFee(cartItem.getProductName(), cartItem.getProductDescription(),
        cartItem.getUnitPrice(), cartItem.getQuantity(), CartItemFeeType.PRICE, FeeTransactionType.DEBIT);
    }

    public static CartItemFee buildPriceItemFee(CartItem cartItem, FeeDto feeDto) {
        return buildCartItemFee(feeDto.getName(), feeDto.getDescription(), feeDto.getAmount(),
                cartItem.getQuantity(), CartItemFeeType.PRICE, FeeTransactionType.DEBIT);
     }

    public static CartItemFee buildDiscountItemFee(Discount discount, BigDecimal discAmount, Integer units) {
        return buildCartItemFee(discount.getName(), discount.getDescription(), discAmount, units,
                CartItemFeeType.DISCOUNT, FeeTransactionType.CREDIT);
    }

    @NotNull
    private static CartItemFee buildCartItemFee(String name, String desc, BigDecimal amount, Integer units,
                                                CartItemFeeType cartItemFeeType, FeeTransactionType transactionType
    ) {
        CartItemFee priceFee = CartItemFee.builder()
                .name(name)
                .description(desc)
                .type(cartItemFeeType)
                .transactionType(transactionType)
                .unitPrice(amount)
                .units(units)
                .build();
        priceFee.setIdentifier(UUID.randomUUID());
        return priceFee;
    }
}
