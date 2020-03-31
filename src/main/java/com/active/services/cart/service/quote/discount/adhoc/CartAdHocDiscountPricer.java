package com.active.services.cart.service.quote.discount.adhoc;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.CartDiscountBasePricer;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartAdHocDiscountPricer extends CartDiscountBasePricer {
    @Override
    public void doQuote(CartQuoteContext context, List<CartItem> noneZeroItems) {
        noneZeroItems.forEach(cartItem -> new CartItemAdHocDiscountPricer().quote(context, cartItem));
    }
}
