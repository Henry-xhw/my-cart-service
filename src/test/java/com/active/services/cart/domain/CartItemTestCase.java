package com.active.services.cart.domain;

import com.active.platform.types.range.Range;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.model.FeeTransactionType;
import com.active.services.cart.model.v1.UpdateCartItemDto;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CartItemTestCase {

    @Test
    public void testGetAllCartItemFees() {
        CartItem cartItem = new CartItem();
        CartItemFee priceFee = CartDataFactory.cartItemFee();
        cartItem.setFees(Arrays.asList(priceFee));

        List<CartItemFee> subItems = new ArrayList<>();
        subItems.add(CartDataFactory.getCartItemFee(FeeTransactionType.CREDIT, CartItemFeeType.PROCESSING_FLAT,
                1, BigDecimal.TEN, "desc1", "name1"));
        subItems.add(CartDataFactory.getCartItemFee(FeeTransactionType.CREDIT, CartItemFeeType.PROCESSING_PERCENT,
                1, BigDecimal.ONE, "desc2", "name2"));
        subItems.add(null);
        subItems.add(CartDataFactory.getCartItemFee(FeeTransactionType.CREDIT, CartItemFeeType.DISCOUNT,
                1, BigDecimal.ONE, "desc3", "name3"));
        priceFee.setSubItems(subItems);

        assertThat(cartItem.getFlattenCartItemFees().size()).isEqualTo(3);
    }

    public void testCartItem() {
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
