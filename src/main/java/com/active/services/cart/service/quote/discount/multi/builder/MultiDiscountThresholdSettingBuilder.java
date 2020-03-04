package com.active.services.cart.service.quote.discount.multi.builder;

import com.active.services.product.discount.multi.MultiDiscount;
import com.active.services.product.discount.multi.MultiDiscountThresholdSetting;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.builder.Builder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
public class MultiDiscountThresholdSettingBuilder implements Builder<Optional<MultiDiscountThresholdSetting>> {

    private MultiDiscount md;

    private long requestThreshold;

    public MultiDiscountThresholdSettingBuilder setMd(MultiDiscount md) {
        this.md = md;

        return this;
    }

    public MultiDiscountThresholdSettingBuilder setRequestThreshold(long requestThreshold) {
        this.requestThreshold = requestThreshold;

        return this;
    }

    @Override
    public Optional<MultiDiscountThresholdSetting> build() {
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
