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

import static com.active.services.cart.service.quote.discount.multi.builder.MultiPersonPricerBuilder.getEffectiveMdThresholdSetting;
import static java.util.stream.Collectors.groupingBy;

@Slf4j
public class MultiProductPricerBuilder implements Builder<List<MultiProductDiscountPricer>> {
    private final MultiDiscountCartItem mdCartItem;

    public MultiProductPricerBuilder(MultiDiscountCartItem mdCartItem) {
        this.mdCartItem = mdCartItem;
    }

    @Override
    public List<MultiProductDiscountPricer> build() {
        List<MultiProductDiscountPricer> pricers = new ArrayList<>();

        Map<String, List<CartItem>> itemsByPersonIdentifier = mdCartItem.getCartItems().stream()
                .collect(groupingBy(CartItem::getPersonIdentifier));
        itemsByPersonIdentifier.forEach((personIdentifier, items) -> {
            Integer productQuantities = items.stream().map(CartItem::getQuantity).reduce(Integer::sum).get();
            Optional<MultiDiscountThresholdSetting> effectiveMdThresholdSettingOpt =
                    getEffectiveMdThresholdSetting(mdCartItem.getMultiDiscount(), productQuantities);
            if (effectiveMdThresholdSettingOpt.isPresent()) {
                pricers.add(new MultiProductDiscountPricer(mdCartItem));
            } else {
                LOG.debug("No threshold met for md {} for person {}",
                        mdCartItem.getMultiDiscount().getId(), personIdentifier);
            }
        });

        return pricers;
    }
}
