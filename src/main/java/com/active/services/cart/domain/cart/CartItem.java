package com.active.services.cart.domain.cart;

import com.active.services.cart.domain.discount.Discount;
import com.active.services.cart.model.BookingDuration;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.model.FeeTransactionType;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

@Data
public class CartItem {
    private Long id;
    private UUID uuid;
    private String referenceId;
    private String orgIdentifier;
    private String personIdentifier;
    private Long productId;
    private Integer quantity;
    private List<BookingDuration> bookingDurations;
    private BigDecimal priceOverride;
    private List<CartItemFee> cartItemFees = new ArrayList<>();
    private List<CartItem> cartItems;

    public BigDecimal getPrice() {
        return cartItemFees.stream()
                .filter(f -> f.getFeeType() == CartItemFeeType.PRICE)
                .map(f -> f.getTransactionType() == FeeTransactionType.DEBIT ? f.getSubtotal() : f.getSubtotal().negate())
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    public CartItem applyDiscount(Discount disc, Currency currency) {
        cartItemFees.stream()
                .filter(f -> f.getFeeType() == CartItemFeeType.PRICE)
                .filter(f -> f.getTransactionType() == FeeTransactionType.DEBIT)
                .forEach(f -> f.applyDiscount(disc, currency));
        return this;
    }
}
