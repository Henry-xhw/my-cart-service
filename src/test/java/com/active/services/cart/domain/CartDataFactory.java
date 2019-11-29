package com.active.services.cart.domain;

import com.active.services.cart.model.CurrencyCode;
import com.active.services.cart.model.Range;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CartDataFactory {

    public static Cart cart() {
        Cart cart = new Cart();

        cart.setId(1L);
        cart.setCurrencyCode(CurrencyCode.USD);
        cart.setKeyerId(UUID.randomUUID());
        cart.setOwnerId(UUID.randomUUID());
        cart.setIdentifier(UUID.randomUUID());
        cart.setItems(cartItems());

        return cart;
    }

    private static List<CartItem> cartItems() {
        return Arrays.asList();
    }

    public static CartItem cartItem() {
        CartItem cartItem = new CartItem();

        cartItem.setIdentifier(UUID.randomUUID());
        cartItem.setProductId(1L);
        cartItem.setProductName("product name");
        cartItem.setProductDescription("product description");
        Range<Instant> bookingRange = new Range<>();
        bookingRange.setLower(Instant.now());
        bookingRange.setUpper(Instant.now());
        cartItem.setBookingRange(bookingRange);
        Range<Instant> trimmedBookingRange = new Range<>();
        trimmedBookingRange.setLower(Instant.now());
        trimmedBookingRange.setUpper(Instant.now());
        cartItem.setTrimmedBookingRange(trimmedBookingRange);
        cartItem.setQuantity(1);
        cartItem.setUnitPrice(BigDecimal.ONE);
        cartItem.setGroupingIdentifier("grouping identifier");

        return cartItem;
    }
}
