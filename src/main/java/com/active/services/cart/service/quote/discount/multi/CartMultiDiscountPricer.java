package com.active.services.cart.service.quote.discount.multi;

import com.active.services.cart.service.quote.CartPricer;
import com.active.services.cart.service.quote.CartQuoteContext;

import org.springframework.stereotype.Component;

@Component
public class CartMultiDiscountPricer implements CartPricer {

    @Override
    public void quote(CartQuoteContext context) {
        // Step1: load multi discounts

    }
}
