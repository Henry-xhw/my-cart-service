package com.active.services.cart.service.quote.discount.adhoc;

import com.active.services.cart.service.quote.CartPricer;
import com.active.services.cart.service.quote.CartQuoteContext;

import org.springframework.stereotype.Component;

@Component
public class CartAdHocDiscountPricer implements CartPricer {
    @Override
    public void quote(CartQuoteContext context) {
        context.getCart().getFlattenCartItems().stream()
                .forEach(cartItem -> new CartItemAdHocDiscountPricer().quote(context, cartItem));
    }
}
