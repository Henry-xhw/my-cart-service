package com.active.services.cart.service.checkout;

import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.inventory.rest.dto.ReservationDTO;

import org.assertj.core.groups.Tuple;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CheckoutContextTestCase {

    @Test
    public void getReservationsWithoutDateRange() {

        CheckoutContext checkoutContext = new CheckoutContext();

        Cart cart = CartDataFactory.cart();
        CartItem cartItem = cart.getFlattenCartItems().get(0);
        cartItem.setOversold(true);
        cartItem.setQuantity(new Integer(3));

        CartItem cartItem2 = cart.getFlattenCartItems().get(1);
        cartItem2.setQuantity(new Integer(23));
        cartItem2.setOversold(false);
        checkoutContext.setCart(cart);

        List<ReservationDTO> reservationDTOS = checkoutContext.getReservations();

        assertEquals(cart.getFlattenCartItems().size(), reservationDTOS.size());
        assertTrue(reservationDTOS.get(0).getAllowOversold());
        assertFalse(reservationDTOS.get(1).getAllowOversold());

        checkReservationDTO(reservationDTOS, 2,
                tuple(cartItem.getProductId(), cartItem.getQuantity(), cartItem.isOversold()),
                tuple(cartItem2.getProductId(), cartItem2.getQuantity(), cartItem2.isOversold()));
    }

    private void checkReservationDTO(List<ReservationDTO> chunks, Integer size, Tuple... tuples) {
        assertThat(chunks).hasSize(size).extracting("productId", "quantity", "allowOversold")
                .contains(tuples);
    }
}
