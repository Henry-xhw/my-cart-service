package com.active.services.cart.mock;

import com.active.platform.types.range.Range;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.v1.CartDto;
import com.active.services.cart.model.v1.CartItemDto;
import com.active.services.cart.model.v1.QuoteCartDto;
import com.active.services.cart.model.v1.QuoteCartItemDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class MockCart {

    public static Cart mockCartDomain() {
        Cart cart = new Cart();
        cart.setCurrencyCode("USD");
        cart.setKeyerId(UUID.randomUUID());
        cart.setOwnerId(UUID.randomUUID());
        cart.setIdentifier(UUID.randomUUID());
        cart.setCouponCodes(Collections.singletonList("FDSAFSA"));
        Instant lower = Instant.parse("2019-11-11T00:00:00Z");
        Instant upper = Instant.parse("2019-11-21T00:00:00Z");
        Range range = new Range();
        range.setLowerInclusive(lower);
        range.setUpperExclusive(upper);
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
        cartDto.setCurrencyCode("USD");
        cartDto.setKeyerId(UUID.randomUUID());
        cartDto.setOwnerId(UUID.randomUUID());
        cartDto.setIdentifier(UUID.randomUUID());
        cartDto.setCouponCodes(Collections.singletonList("FDSAFSA"));
        Instant lower = Instant.parse("2019-11-11T00:00:00Z");
        Instant upper = Instant.parse("2019-11-21T00:00:00Z");
        Range range = new Range();
        range.setLowerInclusive(lower);
        range.setUpperExclusive(upper);
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

    public static QuoteCartDto mockQuoteCartDto() {
        QuoteCartDto cartDto = new QuoteCartDto();
        cartDto.setCurrencyCode("USD");
        cartDto.setKeyerId(UUID.randomUUID());
        cartDto.setOwnerId(UUID.randomUUID());
        cartDto.setIdentifier(UUID.randomUUID());
        Instant lower = Instant.parse("2019-11-11T00:00:00Z");
        Instant upper = Instant.parse("2019-11-21T00:00:00Z");
        Range range = new Range();
        range.setLowerInclusive(lower);
        range.setUpperExclusive(upper);
        QuoteCartItemDto cartItem = new QuoteCartItemDto();
        cartItem.setProductId(1L);
        cartItem.setBookingRange(range);
        cartItem.setGroupingIdentifier("test");
        cartItem.setProductDescription("test");
        cartItem.setQuantity(1);
        cartItem.setProductName("test");
        cartItem.setUnitPrice(BigDecimal.valueOf(1));
        cartItem.setTrimmedBookingRange(range);
        cartItem.setIdentifier(UUID.randomUUID());
        cartItem.setGrossPrice(new BigDecimal(0));
        cartItem.setGrossPrice(new BigDecimal(0));
        cartItem.setFeeVolumeIndex(1);
        List<QuoteCartItemDto> list = new ArrayList<>();
        list.add(cartItem);
        cartDto.setItems(list);
        return cartDto;
    }
}
