package com.active.services.cart.service.quote.discount.multi.builder;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.discount.multi.loader.MultiDiscountCartItem;
import com.active.services.cart.service.quote.discount.multi.pricer.MultiPersonDiscountPricer;
import com.active.services.product.discount.multi.MultiDiscount;
import com.active.services.product.discount.multi.MultiDiscountThresholdSetting;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.builder.Builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
                getEffectiveMdThresholdSetting(mdCartItem.getMultiDiscount(), uniquePersons);
        if (effectiveMdThresholdSettingOpt.isPresent()) {
            return Arrays.asList(new MultiPersonDiscountPricer(mdCartItem, effectiveMdThresholdSettingOpt.get()));
        } else {
            LOG.debug("No threshold met for md {}", mdCartItem.getMultiDiscount().getId());
        }

        return new ArrayList<>();
    }

    public static Optional<MultiDiscountThresholdSetting> getEffectiveMdThresholdSetting(MultiDiscount md,
                                                                                         long requestThreshold) {
        List<MultiDiscountThresholdSetting> thresholdSettings = md.getThresholdSettings();
        if (CollectionUtils.isEmpty(thresholdSettings)) {
            Integer configuredThreshold = md.getThreshold();
            if (requestThreshold >= configuredThreshold) {
                LOG.debug("Hint the single threshold {} by requested threshold {}", md.getId(), requestThreshold);
                return Optional.of(new MultiDiscountThresholdSetting(configuredThreshold, md.getTiers()));
            }
        } else {
            List<MultiDiscountThresholdSetting> settings = md.getThresholdSettings();
            Collections.sort(settings);
            for (MultiDiscountThresholdSetting setting : settings) {
                if (setting.getThreshold() <= requestThreshold) {
                    return Optional.of(setting);
                }
            }
        }

        return Optional.empty();
    }
}
