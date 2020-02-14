package com.active.services.cart.service.quote.discount;

import com.active.services.cart.client.soap.ProductServiceSoap;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartPricer;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.product.DiscountType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CartDiscountPricer implements CartPricer {

    private final ProductServiceSoap productServiceSoap;

    private final DiscountType type;

    @Override
    public void quote(CartQuoteContext context) {
        List<CartItem> cartItems = context.getCart().getFlattenCartItems()
                .stream().sorted(Comparator.comparing(CartItem::getNetPrice)).collect(Collectors.toList());
        cartItems.forEach(item -> getCartItemDiscountPricer(type).quote(context, item));
    }

    @Lookup
    private CartItemDiscountPricer getCartItemDiscountPricer(DiscountType type) {
        return null;
    }
}
