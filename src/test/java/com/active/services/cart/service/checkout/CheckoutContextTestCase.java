package com.active.services.cart.service.checkout;

import com.active.platform.types.range.Range;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

import java.time.Instant;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.assertj.core.api.Assertions.assertThat;

public class CheckoutContextTestCase {

    @Test
    public void testGetReservations() {
        CheckoutContext checkoutContext = new CheckoutContext();
        Cart cart = CartDataFactory.cart();

        CartItem noBookingRangeCartItem = CartDataFactory.cartItem();
        noBookingRangeCartItem.setBookingRange(null);
        cart.getItems().add(noBookingRangeCartItem);

        CartItem nullBookCartItem = CartDataFactory.cartItem();
        nullBookCartItem.setBookingRange(new Range<>(null, null));
        cart.getItems().add(nullBookCartItem);

        checkoutContext.setCart(cart);

        assertThat(checkoutContext.getReservations().size()).isEqualTo(4);
        assertThat(checkoutContext.getReservations().stream().filter(r -> isNotEmpty(r.getDateTimeRanges()))
            .collect(Collectors.toList()).size()).isEqualTo(2);
    }
}
