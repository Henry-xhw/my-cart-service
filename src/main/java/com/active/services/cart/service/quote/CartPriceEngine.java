package com.active.services.cart.service.quote;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

@Component
public class CartPriceEngine {

    public void quote(CartQuoteContext context) {
        getCartPricer().quote(context);
    }

    @Lookup
    public CartUnitPricePricer getCartPricer() {
        return null;
    }
}
