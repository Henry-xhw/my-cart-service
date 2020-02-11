package com.active.services.cart.domain;

import com.active.platform.types.range.Range;
import com.active.services.cart.model.v1.UpdateCartItemDto;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class CartItemTestCase {

    @Test
    public void testCartIteml() {
        UpdateCartItemDto updateCartItemDto = new UpdateCartItemDto();
        updateCartItemDto.setIdentifier(UUID.randomUUID());
        updateCartItemDto.setProductId(RandomUtils.nextLong());
        updateCartItemDto.setProductName("product name");
        updateCartItemDto.setProductDescription("description");
        Range<Instant> bookingRange = new Range<>();
        bookingRange.setLowerInclusive(Instant.now());
        bookingRange.setUpperExclusive(Instant.now());
        updateCartItemDto.setBookingRange(bookingRange);
        Range<Instant> trimmedBookingRange = new Range<>();
        trimmedBookingRange.setLowerInclusive(Instant.now());
        trimmedBookingRange.setUpperExclusive(Instant.now());
        updateCartItemDto.setTrimmedBookingRange(trimmedBookingRange);
        updateCartItemDto.setQuantity(1);
        updateCartItemDto.setUnitPrice(BigDecimal.valueOf(1));
        updateCartItemDto.setGroupingIdentifier("grouping identifier");
        updateCartItemDto.setFeeVolumeIndex(0);
        CartItem cartItem = new CartItem(updateCartItemDto);
        Assert.assertEquals(cartItem.getUnitPrice(), updateCartItemDto.getUnitPrice());
    }
}
