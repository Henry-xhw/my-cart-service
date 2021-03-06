package com.active.services.cart.service.quote.discount.multi.builder;

import com.active.services.product.discount.multi.MultiDiscount;
import com.active.services.product.discount.multi.MultiDiscountThresholdSetting;

import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class EffectiveThresholdSettingBuilderTestCase {
    @Test
    public void getEffectiveMdThresholdSettingSingleThreshold() {
        MultiDiscount md = new MultiDiscount();
        md.setThreshold(10);

        assertThat(getEffectiveMdThresholdSetting(md, 5)).isEmpty();
        assertThat(getEffectiveMdThresholdSetting(md, 10)).isNotEmpty();
        assertThat(getEffectiveMdThresholdSetting(md, 11)).isNotEmpty();
    }

    @Test
    public void getEffectiveMdThresholdSettingMultiThreshold() {
        MultiDiscount md = new MultiDiscount();
        MultiDiscountThresholdSetting setting1 = new MultiDiscountThresholdSetting();
        setting1.setThreshold(15);
        MultiDiscountThresholdSetting setting2 = new MultiDiscountThresholdSetting();
        setting2.setThreshold(5);
        md.setThresholdSettings(Arrays.asList(setting1, setting2));

        assertThat(getEffectiveMdThresholdSetting(md, 3)).isEmpty();
        assertThat(getEffectiveMdThresholdSetting(md, 10)).get()
                .extracting(MultiDiscountThresholdSetting::getThreshold).isEqualTo(5);
        assertThat(getEffectiveMdThresholdSetting(md, 15)).get()
                .extracting(MultiDiscountThresholdSetting::getThreshold).isEqualTo(15);
        assertThat(getEffectiveMdThresholdSetting(md, 16)).get()
                .extracting(MultiDiscountThresholdSetting::getThreshold).isEqualTo(15);
    }

    private Optional<MultiDiscountThresholdSetting> getEffectiveMdThresholdSetting(MultiDiscount md,
                                                                                   int requestThreshold) {
        return new EffectiveThresholdSettingBuilder().setMd(md)
                .setRequestThreshold(requestThreshold).build();
    }
}
