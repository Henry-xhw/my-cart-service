package com.active.services.cart.application.impl;

import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.discount.CartItemsDiscountApplication;
import com.active.services.cart.domain.discount.Discount;
import com.active.services.cart.domain.discount.condition.DiscountSpecification;
import com.active.services.cart.domain.discount.condition.DiscountSpecs;
import com.active.services.cart.infrastructure.repository.ProductRepository;
import com.active.services.product.discount.multi.DiscountTier;
import com.active.services.product.discount.multi.MultiDiscount;
import com.active.services.product.discount.multi.MultiDiscountThresholdSetting;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.cxf.common.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class MultiDiscountEngine {
    @NonNull private final ProductRepository productRepo;
    @NonNull private final DiscountSpecs specs;
    @NonNull private final WithPersonIdCartItemSelector selector;

    public void apply(@NonNull Cart cart) {
        ListMultimap<CartItem, MultiDiscount> item2Mds = ArrayListMultimap.create();
        List<CartItem> items = selector.select(cart);
        for (CartItem item : items) {
            List<MultiDiscount> mds = productRepo.findEffectiveMultiDiscountsByProductId(item.getProductId(),
                    LocalDateTime.now());
            item2Mds.putAll(item, mds);
        }

        ListMultimap<MultiDiscount, CartItem> md2Items = Multimaps.invertFrom(item2Mds, ArrayListMultimap.create());
        for (MultiDiscount md : md2Items.keys()) {
            checkArgument(isNotEmpty(md.getThresholdSettings()) || md.getThreshold() != null,
                    "MultiDiscount doesn't allow threshold and thresholdSettings to be null or empty at the same time");

            List<Discount> discounts = new ArrayList<>();
            if (isEmpty(md.getThresholdSettings())) {
                DiscountSpecification spec =
                        specs.multiDiscountThreshold(md.getDiscountType(), md2Items.get(md), md.getThreshold());
                md.getTiers().stream()
                        .sorted(Comparator.comparingInt(DiscountTier::getTierLevel))
                        .map(t -> new Discount(md.getName(), md.getDescription(), t.getAmount(), t.getAmountType()))
                        .map(d -> d.setCondition(spec))
                        .forEachOrdered(discounts::add);
            } else {
                Collections.sort(md.getThresholdSettings());
                for (MultiDiscountThresholdSetting s : md.getThresholdSettings()) {
                    DiscountSpecification spec =
                            specs.multiDiscountThreshold(md.getDiscountType(), md2Items.get(md), s.getThreshold());
                    s.getTiers().stream()
                            .sorted(Comparator.comparingInt(DiscountTier::getTierLevel))
                            .map(t -> new Discount(md.getName(), md.getDescription(), t.getAmount(), t.getAmountType()))
                            .map(d -> d.setCondition(spec))
                            .forEachOrdered(discounts::add);
                }
            }
            CartItemsDiscountApplication da = new CartItemsDiscountApplication(md2Items.get(md), discounts, null,
                    cart.getCurrencyCode());
        }
    }
}
