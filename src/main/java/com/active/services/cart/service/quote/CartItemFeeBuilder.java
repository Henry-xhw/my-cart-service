package com.active.services.cart.service.quote;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.domain.Discount;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.model.FeeTransactionType;
import com.active.services.contract.controller.v1.FeeAmountResult;
import com.active.services.contract.controller.v1.type.FeeType;
import com.active.services.product.nextgen.v1.dto.fee.FeeDto;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public class CartItemFeeBuilder {

    public static CartItemFee buildOverridePriceItemFee(CartItem cartItem) {
        return buildCartItemFee(cartItem.getProductName(), cartItem.getProductDescription(),
        cartItem.getUnitPrice(), cartItem.getQuantity(), CartItemFeeType.PRICE, FeeTransactionType.DEBIT, null);
    }

    public static CartItemFee buildPriceItemFee(Integer quantity, FeeDto feeDto) {
        return buildCartItemFee(feeDto.getName(), feeDto.getDescription(), feeDto.getAmount(),
                quantity, CartItemFeeType.PRICE, FeeTransactionType.DEBIT, null);
     }

    public static CartItemFee buildDiscountItemFee(Discount discount, BigDecimal discAmount, Integer quantity) {
        return buildCartItemFee(discount.getName(), discount.getDescription(), discAmount, quantity,
                CartItemFeeType.DISCOUNT, FeeTransactionType.CREDIT, discount.getIdentifier());

    }

    public static CartItemFee buildActiveFeeCartItemFee(Integer quantity, FeeAmountResult feeAmountResult) {
        return buildCartItemFee(feeAmountResult.getDescription(), feeAmountResult.getDescription(),
                feeAmountResult.getAmount(), quantity, getFeeType(feeAmountResult.getFeeType()),
                getTranactionType(feeAmountResult.getFeeType()), null);
    }

    private static CartItemFeeType getFeeType(FeeType feeType) {
        if (feeType == FeeType.PERCENT) {
            return CartItemFeeType.PROCESSING_PERCENT;
        }
        return CartItemFeeType.PROCESSING_FLAT;
    }

    private static FeeTransactionType getTranactionType(FeeType feeType) {
        if (feeType == FeeType.FLAT_ADJUSTMENT_MAX) {
            return FeeTransactionType.CREDIT;
        }
        return FeeTransactionType.DEBIT;
    }

    @NotNull
    private static CartItemFee buildCartItemFee(String name, String desc, BigDecimal amount, Integer units,
                                                CartItemFeeType cartItemFeeType, FeeTransactionType transactionType,
                                                UUID identifier) {
        return CartItemFee.builder()
                .name(name)
                .description(desc)
                .type(cartItemFeeType)
                .transactionType(transactionType)
                .unitPrice(amount)
                .units(units)
                .relatedIdentifier(identifier)
                .build();
    }
}
