package com.active.services.cart.mock;

import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.CurrencyCode;
import com.active.services.cart.model.Range;
import com.active.services.cart.model.v1.CartDto;
import com.active.services.cart.model.v1.CartItemDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MockCart {

    public static Cart mockCartDomain() {
        Cart cart = new Cart();
        cart.setCurrencyCode(CurrencyCode.USD);
        cart.setKeyerId(UUID.randomUUID());
        cart.setOwnerId(UUID.randomUUID());
        cart.setIdentifier(UUID.randomUUID());
        Instant lower = Instant.parse("2019-11-11T00:00:00Z");
        Instant upper = Instant.parse("2019-11-21T00:00:00Z");
        Range range = new Range();
        range.setLower(lower);
        range.setUpper(upper);
        CartItem cartItem = new CartItem();
        cartItem.setProductId(1L);
        cartItem.setBookingRange(range);
        cartItem.setGroupingIdentifier("test");
        cartItem.setProductDescription("test");
        cartItem.setQuantity(1);
        cartItem.setProductName("test");
        cartItem.setUnitPrice(BigDecimal.valueOf(1));
        cartItem.setTrimmedBookingRange(range);
        cartItem.setIdentifier(UUID.randomUUID());
        cartItem.setId(1L);
        cartItem.setFeeVolumeIndex(1);
        List<CartItem> list = new ArrayList<>();
        list.add(cartItem);
        cart.setItems(list);
        return cart;
    }

    public static CartDto mockCartDto() {
        CartDto cartDto = new CartDto();
        cartDto.setCurrencyCode(CurrencyCode.USD);
        cartDto.setKeyerId(UUID.randomUUID());
        cartDto.setOwnerId(UUID.randomUUID());
        cartDto.setIdentifier(UUID.randomUUID());
        Instant lower = Instant.parse("2019-11-11T00:00:00Z");
        Instant upper = Instant.parse("2019-11-21T00:00:00Z");
        Range range = new Range();
        range.setLower(lower);
        range.setUpper(upper);
        CartItemDto cartItem = new CartItemDto();
        cartItem.setProductId(1L);
        cartItem.setBookingRange(range);
        cartItem.setGroupingIdentifier("test");
        cartItem.setProductDescription("test");
        cartItem.setQuantity(1);
        cartItem.setProductName("test");
        cartItem.setUnitPrice(BigDecimal.valueOf(1));
        cartItem.setTrimmedBookingRange(range);
        cartItem.setIdentifier(UUID.randomUUID());
        cartItem.setFeeVolumeIndex(1);
        List<CartItemDto> list = new ArrayList<>();
        list.add(cartItem);
        cartDto.setItems(list);
        return cartDto;
    }
}
