package com.active.services.cart.domain.cart;

import com.active.services.cart.model.BookingDuration;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
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
    private List<CartItemFee> cartItemFees;
    private List<CartItem> cartItems;
}
