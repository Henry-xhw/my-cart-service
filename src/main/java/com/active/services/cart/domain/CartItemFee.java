package com.active.services.cart.domain;

import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.model.FeeTransactionType;
import com.active.services.cart.service.quote.discount.Discount;
import com.active.services.product.nextgen.v1.dto.fee.FeeDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Builder
@AllArgsConstructor
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

    public void applyDiscount(Discount disc, String currency) {
        if (getSubItems() == null) {
            setSubItems(new ArrayList<>());
        }
        BigDecimal discAmount = disc.apply(unitPrice, Currency.getInstance(currency));
        getSubItems().add(CartItemFee.builder()
                .name(disc.getName())
                .description(disc.getDescription())
                .type(CartItemFeeType.DISCOUNT)
                .transactionType(FeeTransactionType.CREDIT)
                .unitPrice(discAmount)
                .units(units)
                .build());
        unitPrice = unitPrice.subtract(discAmount);
    }
}
