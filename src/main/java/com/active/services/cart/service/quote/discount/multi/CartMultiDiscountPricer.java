package com.active.services.cart.service.quote.discount.multi;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.CartDiscountBasePricer;
import com.active.services.cart.service.quote.discount.multi.builder.MultiDiscountPricerBuilder;
import com.active.services.cart.service.quote.discount.multi.loader.MultiDiscountCartItem;
import com.active.services.cart.service.quote.discount.multi.loader.MultiDiscountLoader;
import com.active.services.cart.service.quote.discount.multi.pricer.MultiDiscountPricer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class CartMultiDiscountPricer extends CartDiscountBasePricer {

    @Autowired
    private MultiDiscountLoader multiDiscountLoader;

    @Override
    protected void doQuote(CartQuoteContext context, List<CartItem> noneZeroItems) {
        // Step1: load multi discounts
        List<MultiDiscountCartItem> mdCartItems = multiDiscountLoader.load(context.getCart());

        // Step2: build pricer by MultiDiscountComparator order.
        Collections.sort(mdCartItems);
        List<MultiDiscountPricer> pricers = new MultiDiscountPricerBuilder()
                .multiDiscountCartItems(mdCartItems).build();

        // Step3: run pricer
        pricers.forEach(MultiDiscountPricer::price);
    }
}
