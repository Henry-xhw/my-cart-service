package com.active.services.cart.application.impl;

import com.active.services.cart.domain.cart.Cart;
import com.active.services.cart.domain.cart.CartItem;
import com.active.services.cart.domain.discount.CartItemsDiscountApplication;
import com.active.services.cart.domain.discount.Discount;
import com.active.services.cart.domain.discount.condition.DiscountSpecification;
import com.active.services.cart.domain.discount.condition.DiscountSpecs;
import com.active.services.cart.infrastructure.repository.ProductRepository;
import com.active.services.product.discount.multi.MultiDiscount;
import com.active.services.product.discount.multi.MultiDiscountThresholdSetting;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.cxf.common.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
public class MultiDiscountEngine {
    @NonNull private final ProductRepository productRepo;
    @NonNull private final DiscountSpecs specs;

    public void apply(Cart cart) {
        ListMultimap<CartItem, MultiDiscount> item2Mds = new ArrayListMultimap<>();
        for (CartItem item : cart.getCartItems()) {
            List<MultiDiscount> mds = productRepo.findEffectiveMultiDiscountsByProductId(item.getProductId(), cart.getPriceDate());
            item2Mds.putAll(item, mds);
        }

        ListMultimap<MultiDiscount, CartItem> md2Items = Multimaps.invertFrom(item2Mds, ArrayListMultimap.create());
        for (MultiDiscount md : md2Items.keys()) {
            checkArgument(isNotEmpty(md.getThresholdSettings()) || md.getThreshold() != null,
                    "MultiDiscount doesn't allow threshold and thresholdSettings to be null or empty at the same time");

            List<Discount> discounts = new ArrayList<>();
            if (isEmpty(md.getThresholdSettings())) {
                DiscountSpecification spec = specs.multiDiscountThreshold(md.getDiscountType(), md2Items.get(md), md.getThreshold());
                md.getTiers().stream()
                        .map(t -> new Discount(md.getName(), md.getDescription(), t.getAmount(), t.getAmountType()))
                        .map(d -> d.setCondition(spec))
                        .forEachOrdered(discounts::add);
            } else {
                for (MultiDiscountThresholdSetting s : md.getThresholdSettings()) {
                    DiscountSpecification spec = specs.multiDiscountThreshold(md.getDiscountType(), md2Items.get(md), s.getThreshold());
                    s.getTiers().stream()
                            .map(t -> new Discount(md.getName(), md.getDescription(), t.getAmount(), t.getAmountType()))
                            .map(d -> d.setCondition(spec))
                            .forEachOrdered(discounts::add);
                }
            }
            CartItemsDiscountApplication da = new CartItemsDiscountApplication(md2Items.get(md), discounts, null, cart.getCurrency());
        }
    }
}
