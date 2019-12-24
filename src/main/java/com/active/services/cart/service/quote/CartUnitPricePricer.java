package com.active.services.cart.service.quote;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CartUnitPricePricer implements CartPricer {
    @Override
    public void quote(CartQuoteContext context) {
        context.getCart().getItems().forEach(cartItem -> {
            getCartItemPricer().quote(context, cartItem);
        });
    }

    @Lookup
    public CartItemUnitPricePricer getCartItemPricer() {
        return null;
    }
}
