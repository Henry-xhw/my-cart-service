package com.active.services.cart.service.quote.discount.adhoc;

import com.active.services.cart.BaseTestCase;
import com.active.services.cart.domain.AdHocDiscount;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.service.quote.CartQuoteContext;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class CartAdHocDiscountPricerTestCase extends BaseTestCase {

    @Test
    public void testQuoteSuccess() {
        Cart cart = CartDataFactory.cart();
        CartQuoteContext context = new CartQuoteContext(cart);
        cart.getFlattenCartItems().forEach(cartItem -> {
            AdHocDiscount adHocDiscount1 = new AdHocDiscount();
            adHocDiscount1.setDiscountAmount(BigDecimal.valueOf(10));
            adHocDiscount1.setId(1L);
            AdHocDiscount adHocDiscount2 = new AdHocDiscount();
            adHocDiscount2.setDiscountAmount(BigDecimal.ONE);
            adHocDiscount2.setDiscountName("test ad-hoc discount");
            adHocDiscount2.setId(2L);
            cartItem.setAdHocDiscounts(Arrays.asList(adHocDiscount1, adHocDiscount2));
        });
        CartAdHocDiscountPricer cartAdHocDiscountPricer = new CartAdHocDiscountPricer();
        cartAdHocDiscountPricer.quote(context);
        Assert.assertNotNull(cart.getItems().get(0).getPriceCartItemFee().get().getSubItems());
        Assert.assertTrue(cart.getItems().get(0).getPriceCartItemFee().get().getSubItems().size() > 0);
    }

    @Test
    public void testQuoteWhenPriceFeeIsNull() {
        Cart cart = CartDataFactory.cart();
        CartQuoteContext context = new CartQuoteContext(cart);
        cart.getFlattenCartItems().forEach(cartItem -> {
            cartItem.setFees(new ArrayList<>());
        });
        CartAdHocDiscountPricer cartAdHocDiscountPricer = new CartAdHocDiscountPricer();
        cartAdHocDiscountPricer.quote(context);
        Assert.assertEquals(cart.getItems().get(0).getPriceCartItemFee(), Optional.empty());
    }

    @Test
    public void testQuoteWhenNetPriceLessThanZero() {
        Cart cart = CartDataFactory.cart();
        CartQuoteContext context = new CartQuoteContext(cart);
        cart.getFlattenCartItems().forEach(cartItem -> {
            cartItem.getFees().forEach(cartItemFee -> cartItemFee.setUnitPrice(BigDecimal.ZERO));
        });
        CartAdHocDiscountPricer cartAdHocDiscountPricer = new CartAdHocDiscountPricer();
        cartAdHocDiscountPricer.quote(context);
        Assert.assertFalse(cart.getItems().get(0).getPriceCartItemFee().get().getSubItems().size() > 0);
    }
}
