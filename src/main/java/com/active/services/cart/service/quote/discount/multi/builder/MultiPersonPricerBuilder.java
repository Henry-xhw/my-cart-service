package com.active.services.cart.service.quote.discount.multi.builder;

import com.active.services.cart.service.quote.discount.multi.loader.MultiDiscountCartItem;
import com.active.services.cart.service.quote.discount.multi.pricer.MultiPersonDiscountPricer;
import com.active.services.product.discount.multi.MultiDiscountThresholdSetting;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.Builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.active.services.cart.service.quote.discount.multi.MultiDiscountUtil.uniquePerson;

@Slf4j
public class MultiPersonPricerBuilder implements Builder<List<MultiPersonDiscountPricer>> {
    private final MultiDiscountCartItem mdCartItem;

    public MultiPersonPricerBuilder(MultiDiscountCartItem mdCartItem) {
        this.mdCartItem = mdCartItem;
    }

    @Override
    public List<MultiPersonDiscountPricer> build() {
        long uniquePersons = uniquePerson(mdCartItem.getCartItems());
        Optional<MultiDiscountThresholdSetting> effectiveMdThresholdSettingOpt =
            new MultiDiscountThresholdSettingBuilder().setMd(mdCartItem.getMultiDiscount())
                    .setRequestThreshold(uniquePersons).build();
        if (effectiveMdThresholdSettingOpt.isPresent()) {
            return Arrays.asList(new MultiPersonDiscountPricer(mdCartItem, effectiveMdThresholdSettingOpt.get()));
        } else {
            LOG.debug("No threshold met for md {}", mdCartItem.getMultiDiscount().getId());
        }

        return new ArrayList<>();
    }
}
