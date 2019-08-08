package com.active.services.cart.domain.rule;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.active.services.cart.application.RuleEngine;
import com.active.services.cart.domain.rule.product.ProductFact;
import com.active.services.domain.DateTime;
import com.active.services.product.Fee;
import com.active.services.product.FeeType;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductRuleTestCase {
    @Autowired private RuleEngine ruleEngine;

    @Test
    public void timeBasedPrice() {
        List<Fee> fees = new ArrayList<>();
        Fee f = new Fee();
        f.setAmount(new BigDecimal(ThreadLocalRandom.current().nextInt(1, 1000)));
        f.setStartDate(new DateTime(LocalDateTime.of(2019, 7, 1, 0, 0, 0)));
        f.setEndDate(new DateTime(LocalDateTime.of(2019, 8, 1, 0, 0, 0)));
        fees.add(f);
        ProductFact pf = ProductFact.builder()
                .feeType(FeeType.TIME_BASED)
                .fees(fees)
                .pricingDt(new DateTime(LocalDateTime.now()))
                .build();
        ruleEngine.runRule(Collections.singletonList(pf));

        assertThat(pf.getResult()).isNotNull().isEqualTo(f);
    }

    @Test
    public void volumeBasedPrice() {
        List<Fee> fees = new ArrayList<>();
        Fee f = new Fee();
        f.setAmount(new BigDecimal(ThreadLocalRandom.current().nextInt(1, 1000)));
        f.setMinVolume(ThreadLocalRandom.current().nextInt(1, 100));
        f.setMaxVolume(ThreadLocalRandom.current().nextInt(100, 200));
        fees.add(f);
        ProductFact pf = ProductFact.builder()
                .feeType(FeeType.VOLUME_BASED)
                .fees(fees)
                .pricingDt(new DateTime(LocalDateTime.now()))
                .volume(ThreadLocalRandom.current().nextInt(1, 200))
                .build();
        ruleEngine.runRule(Collections.singletonList(pf));

        assertThat(pf.getResult()).isNotNull().isEqualTo(f);
    }
}
