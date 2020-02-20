package com.active.services.cart.service.quote.discount.coupon;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartItemPricer;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.Discount;
import com.active.services.cart.service.quote.discount.algorithm.DiscountsAlgorithms;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CartItemCouponPricer implements CartItemPricer {

    private final List<Discount> discounts;

    @Override
    public void quote(CartQuoteContext context, CartItem cartItem) {
        DiscountsAlgorithms.getAlgorithm(cartItem, context.getDiscountModel(cartItem.getProductId()),
                context.getCurrency()).apply(null);
                //.forEach(disc -> new DiscountFeeLoader(cartQuoteContext, item, disc).apply());
        // Kimi: suggest to put the DiscountFeeLoader logic here.


    }
}
