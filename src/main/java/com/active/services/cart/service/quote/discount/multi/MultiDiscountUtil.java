package com.active.services.cart.service.quote.discount.multi;

import com.active.services.cart.domain.CartItem;
import com.active.services.product.discount.multi.MultiDiscount;
import com.active.services.product.discount.multi.MultiDiscountThresholdSetting;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
public final class MultiDiscountUtil {
    private MultiDiscountUtil() {

    }

    public static int uniquePerson(List<CartItem> cartItems) {
        return Long.valueOf(cartItems.stream().map(CartItem::getPersonIdentifier).distinct().count()).intValue();
    }

    public static int quantityCounts(List<CartItem> cartItems) {
        return cartItems.stream().map(CartItem::getQuantity).reduce(Integer::sum).orElse(0);
    }

    public static Map<String, List<CartItem>> itemsByPersonIdentifier(List<CartItem> cartItems) {
        return cartItems.stream().collect(groupingBy(CartItem::getPersonIdentifier));
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
