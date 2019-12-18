package com.active.services.cart.domain;

import com.active.services.cart.model.CartItemFeeTransactionType;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.model.CurrencyCode;
import com.active.services.cart.model.Range;
import com.active.services.cart.model.v1.UpdateCartItemDto;

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
        cartItem.setAgencyId(123L);
        cartItem.setFeeVolumeIndex(0);

        return cartItem;
    }

    public static UpdateCartItemDto updateCartItemDto(CartItem cartItem) {
        UpdateCartItemDto updateCartItemDto = new UpdateCartItemDto();
        updateCartItemDto.setBookingRange(cartItem.getBookingRange());
        updateCartItemDto.setGroupingIdentifier(cartItem.getGroupingIdentifier());
        updateCartItemDto.setIdentifier(cartItem.getIdentifier());
        updateCartItemDto.setProductDescription(cartItem.getProductDescription());
        updateCartItemDto.setProductId(cartItem.getProductId());
        updateCartItemDto.setQuantity(cartItem.getQuantity());
        updateCartItemDto.setUnitPrice(cartItem.getUnitPrice());
        updateCartItemDto.setTrimmedBookingRange(cartItem.getTrimmedBookingRange());
        updateCartItemDto.setProductName(cartItem.getProductName());
        return updateCartItemDto;
    }

    public static CartItemFee cartItemFee() {
        CartItemFee cartItemFee = new CartItemFee();
        cartItemFee.setId(1L);
        cartItemFee.setIdentifier(UUID.randomUUID());
        cartItemFee.setDescription("Description");
        cartItemFee.setName("name");
        cartItemFee.setTransactionType(CartItemFeeTransactionType.CREDIT);
        cartItemFee.setType(CartItemFeeType.DISCOUNT);
        cartItemFee.setUnitPrice(new BigDecimal(1));
        cartItemFee.setUnits(1);
        return cartItemFee;
    }
}
