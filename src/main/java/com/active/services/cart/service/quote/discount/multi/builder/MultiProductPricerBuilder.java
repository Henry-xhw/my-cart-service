package com.active.services.cart.service.quote.discount.multi.builder;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.discount.multi.loader.MultiDiscountCartItem;
import com.active.services.cart.service.quote.discount.multi.pricer.MultiProductDiscountPricer;
import com.active.services.product.discount.multi.MultiDiscountThresholdSetting;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.active.services.cart.service.quote.discount.multi.MultiDiscountUtil.itemsByPersonIdentifier;
import static com.active.services.cart.service.quote.discount.multi.MultiDiscountUtil.quantityCounts;
import static com.active.services.cart.service.quote.discount.multi.builder.MultiPersonPricerBuilder.getEffectiveMdThresholdSetting;

@Slf4j
public class MultiProductPricerBuilder implements Builder<List<MultiProductDiscountPricer>> {
    private final MultiDiscountCartItem mdCartItem;

    public MultiProductPricerBuilder(MultiDiscountCartItem mdCartItem) {
        this.mdCartItem = mdCartItem;
    }

    @Override
    public List<MultiProductDiscountPricer> build() {
        List<MultiProductDiscountPricer> pricers = new ArrayList<>();

        Map<String, List<CartItem>> itemsByIdentifier = itemsByPersonIdentifier(mdCartItem.getCartItems());
        itemsByIdentifier.forEach((personIdentifier, items) -> {
            int productQuantities = quantityCounts(items);
            Optional<MultiDiscountThresholdSetting> effectiveMdThresholdSettingOpt =
                    getEffectiveMdThresholdSetting(mdCartItem.getMultiDiscount(), productQuantities);
            if (effectiveMdThresholdSettingOpt.isPresent()) {
                pricers.add(new MultiProductDiscountPricer(mdCartItem, effectiveMdThresholdSettingOpt.get()));
            } else {
                LOG.debug("No threshold met for md {} for person {}",
                        mdCartItem.getMultiDiscount().getId(), personIdentifier);
            }
        });

        return pricers;
    }
}
