package com.active.services.cart.service.quote.discount.processor;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartItemPricer;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.Discount;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
public class CartItemDiscountPricer implements CartItemPricer {
    @NonNull private final DiscountHandler handler;

    @Override
    public void quote(CartQuoteContext context, CartItem cartItem) {
        List<Discount> discounts = handler.loadDiscounts();
        if (CollectionUtils.isEmpty(discounts)) {
            return;
        }
        if (cartItem.getNetPrice().compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }
        handler.getDiscountAlgorithm().apply(discounts).forEach(disc ->
                new DiscountFeeLoader(context, cartItem, disc).apply());
    }
}
