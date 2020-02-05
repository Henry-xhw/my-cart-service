package com.active.services.cart.service.quote;

import com.active.services.contract.controller.v1.FeeOwner;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

@Component
public class CartPriceEngine {

    public void quote(CartQuoteContext context) {
        getCartUnitPricePricer().quote(context);
//        getProductProcessingFeePricer().quote(context, FeeOwner.CONSUMER);
    }

    @Lookup
    public CartUnitPricePricer getCartUnitPricePricer() {
        return null;
    }

    @Lookup
    public ProductProcessingFeePricer getProductProcessingFeePricer() {
        return null;
    }
}
