package com.active.services.cart.domain;

import com.active.platform.types.range.Range;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.model.FeeTransactionType;
import com.active.services.cart.model.v1.UpdateCartItemDto;
import com.active.services.cart.service.CartStatus;

import org.apache.commons.lang3.RandomUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CartDataFactory {

    public static Cart cart() {
        Cart cart = new Cart();

        cart.setId(1L);
        cart.setCurrencyCode("USD");
        cart.setKeyerId(UUID.randomUUID());
        cart.setOwnerId(UUID.randomUUID());
        cart.setIdentifier(UUID.randomUUID());
        cart.setItems(cartItems());
        cart.setCartStatus(CartStatus.CREATED);

        return cart;
    }

    private static List<CartItem> cartItems() {
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem());
        cartItems.add(cartItem());
        return cartItems;
    }

    public static CartItem cartItem() {
        return getCartItem(1, new BigDecimal(10), "description");
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
        return getCartItemFee(FeeTransactionType.CREDIT, CartItemFeeType.PRICE, 1, new BigDecimal(1), "description",
                "name");
    }

    public static CartItemFee getCartItemFee(FeeTransactionType transactionType, CartItemFeeType feeType, int unit,
                                              BigDecimal price,
                                              String description,
                                              String name) {
        CartItemFee cartItemFee = new CartItemFee();
        cartItemFee.setId(RandomUtils.nextLong());
        cartItemFee.setIdentifier(UUID.randomUUID());
        cartItemFee.setTransactionType(transactionType);
        cartItemFee.setType(feeType);
        cartItemFee.setUnits(unit);
        cartItemFee.setUnitPrice(price);
        cartItemFee.setDescription(description);
        cartItemFee.setName(name);
        return cartItemFee;
    }

    public static CartItem getCartItem(Integer quantity, BigDecimal price, String description) {
        CartItem cartItem = new CartItem();
        cartItem.setIdentifier(UUID.randomUUID());
        cartItem.setProductId(RandomUtils.nextLong());
        cartItem.setProductName("product name");
        cartItem.setProductDescription(description);
        Range<Instant> bookingRange = new Range<>();
        bookingRange.setLowerInclusive(Instant.now());
        bookingRange.setUpperExclusive(Instant.now());
        cartItem.setBookingRange(bookingRange);
        Range<Instant> trimmedBookingRange = new Range<>();
        trimmedBookingRange.setLowerInclusive(Instant.now());
        trimmedBookingRange.setUpperExclusive(Instant.now());
        cartItem.setTrimmedBookingRange(trimmedBookingRange);
        cartItem.setQuantity(quantity);
        cartItem.setUnitPrice(price);
        cartItem.setGroupingIdentifier("grouping identifier");
        cartItem.setFeeVolumeIndex(0);
        List<CartItemFee> cartItemFees = new ArrayList<>();
        cartItemFees.add(cartItemFee());
        cartItemFees.add(cartItemFee());
        cartItem.setFees(cartItemFees);
        return cartItem;
    }
}
