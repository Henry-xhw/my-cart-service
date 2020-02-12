package com.active.services.cart.service.checkout;

import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.ErrorCode;
import com.active.services.cart.service.CartStatus;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CheckoutValidatorTestCase {

    @Test
    public void checkoutWhenCartWithoutCartItem() {
        Cart cart = getQualifiedCart(UUID.randomUUID());
        cart.setItems(new ArrayList<>());
        CheckoutContext checkoutContext = new CheckoutContext();
        checkoutContext.setCart(cart);

        try {
            CheckoutValidator validator = new CheckoutValidator();
            validator.validate(checkoutContext);
            fail("should fail when there is no cart item");
        } catch (CartException e) {
            assertEquals(ErrorCode.CART_ITEM_NOT_FOUND, e.getErrorCode());
        }
    }

    @Test
    public void checkoutWhenCartWithUnMatchedPricing() {
        UUID cartId = UUID.randomUUID();
        Cart cart = getQualifiedCart(cartId);
        cart.setVersion(3);
        CheckoutContext checkoutContext = new CheckoutContext();
        checkoutContext.setCart(cart);

        try {
            CheckoutValidator validator = new CheckoutValidator();
            validator.validate(checkoutContext);
            fail("should fail when pricing out of date");
        } catch (CartException e) {
            assertEquals(ErrorCode.CART_PRICING_OUT_OF_DATE, e.getErrorCode());
        }
    }

    @Test
    public void checkoutWhenCartFinalized() {
        UUID cartId = UUID.randomUUID();
        Cart cart = getQualifiedCart(cartId);
        cart.setCartStatus(CartStatus.FINALIZED);
        CheckoutContext checkoutContext = new CheckoutContext();
        checkoutContext.setCart(cart);

        try {
            CheckoutValidator validator = new CheckoutValidator();
            validator.validate(checkoutContext);
            fail("should fail when cart finalized");
        } catch (CartException e) {
            assertEquals(ErrorCode.VALIDATION_ERROR, e.getErrorCode());
        }
    }

    @Test
    public void checkoutPass() {
        UUID cartId = UUID.randomUUID();
        Cart cart = getQualifiedCart(cartId);
        CheckoutContext checkoutContext = new CheckoutContext();
        checkoutContext.setCart(cart);

        try {
            CheckoutValidator validator = new CheckoutValidator();
            validator.validate(checkoutContext);
        } catch (CartException e) {
            fail("should pass validation");
        }
    }

    private Cart getQualifiedCart(UUID cartId) {
        Cart cart = CartDataFactory.cart();
        cart.setIdentifier(cartId);
        List<CartItem> items = new ArrayList<>();
        items.add(CartDataFactory.cartItem());
        cart.setItems(items);
        cart.setVersion(1);
        cart.setPriceVersion(1);
        return cart;
    }

}
