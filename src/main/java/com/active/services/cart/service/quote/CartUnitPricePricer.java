package com.active.services.cart.service.quote;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.util.TreeBuilder;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CartUnitPricePricer implements CartPricer {
    @Override
    public void quote(CartQuoteContext context) {
        List<CartItem> flattenCartItems = context.getCart().getFlattenCartItems();
        flattenCartItems.forEach(cartItem -> {
            getCartItemPricer().quote(context, cartItem);
        });
        TreeBuilder<CartItem> baseTreeTreeBuilder = new TreeBuilder<>(flattenCartItems);
        context.getCart().setItems(baseTreeTreeBuilder.buildTree());
    }

    @Lookup
    public CartItemUnitPricePricer getCartItemPricer() {
        return null;
    }
}
