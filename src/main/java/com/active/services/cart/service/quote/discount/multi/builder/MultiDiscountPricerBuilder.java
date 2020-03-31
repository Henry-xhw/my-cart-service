package com.active.services.cart.service.quote.discount.multi.builder;

import com.active.services.cart.service.quote.discount.multi.loader.MultiDiscountCartItem;
import com.active.services.cart.service.quote.discount.multi.pricer.MultiDiscountPricer;
import com.active.services.product.discount.multi.MultiDiscountType;

import org.apache.commons.lang3.builder.Builder;

import java.util.ArrayList;
import java.util.List;

/**
 * Build MultiDiscountPricer by loaded multi discount/cart items mapping.
 *
 */
public class MultiDiscountPricerBuilder implements Builder<List<MultiDiscountPricer>> {

    private List<MultiDiscountCartItem> mdCartItems;

    public MultiDiscountPricerBuilder multiDiscountCartItems(List<MultiDiscountCartItem> mdCartItems) {
        this.mdCartItems = mdCartItems;

        return this;
    }

    @Override
    public List<MultiDiscountPricer> build() {
        List<MultiDiscountPricer> pricers = new ArrayList<>();

        mdCartItems.forEach(mdCartItem -> {
            MultiDiscountType dt = mdCartItem.getMultiDiscount().getDiscountType();
            switch(dt) {
                case MULTI_PERSON:
                    pricers.addAll(new MultiPersonPricerBuilder(mdCartItem).build());
                    break;
                case MULTI_PRODUCT:
                    pricers.addAll(new MultiProductPricerBuilder(mdCartItem).build());
                    break;
                default:
                    throw new IllegalStateException("Does not know how to handle multi discount type " + dt);
            }
        });

        return pricers;
    }
}
