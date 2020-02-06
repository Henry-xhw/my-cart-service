package com.active.services.cart.service.quote;

import com.active.services.cart.client.soap.ProductServiceSoap;
import com.active.services.contract.controller.v1.FeeOwner;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartPriceEngine {
    private final ProductServiceSoap productServiceSoap;

    public void quote(CartQuoteContext context) {
        context.setProducts(productServiceSoap.getProductsByCartItems(context.getCart().getFlattenCartItems()));
        getCartUnitPricePricer().quote(context);
        getCartProductProcessingFeePricer(FeeOwner.CONSUMER).quote(context);
    }

    @Lookup
    public CartUnitPricePricer getCartUnitPricePricer() {
        return null;
    }

    @Lookup
    public CartProductProcessingFeePricer getCartProductProcessingFeePricer(FeeOwner feeOwner) {
        return null;
    }
}
