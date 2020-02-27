package com.active.services.cart.domain;

import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.model.FeeTransactionType;

import org.junit.Test;

import java.math.BigDecimal;
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
        UUID relatedIdentifier = UUID.randomUUID();
        List<CartItemFee> subItems = new ArrayList<>();
        subItems.add(CartDataFactory.getCartItemFee(FeeTransactionType.DEBIT, CartItemFeeType.PROCESSING_FLAT,
                1, BigDecimal.TEN, "desc1", "name1", relatedIdentifier));
        subItems.add(CartDataFactory.getCartItemFee(FeeTransactionType.DEBIT, CartItemFeeType.PROCESSING_PERCENT,
                1, BigDecimal.ONE, "desc2", "name2", relatedIdentifier));
        subItems.add(null);
        subItems.add(CartDataFactory.getCartItemFee(FeeTransactionType.CREDIT, CartItemFeeType.DISCOUNT,
                1, BigDecimal.ONE, "desc3", "name3", relatedIdentifier));
        priceFee.setSubItems(subItems);

        assertThat(cartItem.getFlattenCartItemFees().size()).isEqualTo(4);
    }

}
