package com.active.services.cart.infrastructure.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import com.active.services.cart.domain.rule.Condition;
import com.active.services.cart.domain.rule.Rule;
import com.active.services.cart.domain.rule.fee.ConditionGroup;
import com.active.services.cart.domain.rule.fee.LocalDateRangeCondition;
import com.active.services.cart.domain.rule.fee.LocalTimeRangeCondition;
import com.active.services.cart.domain.rule.fee.LongRangeCondition;
import com.active.services.cart.domain.rule.fee.SimpleCondition;
import com.active.services.cart.domain.rule.product.ProductPriceRule;
import com.active.services.product.Fee;
import com.active.services.product.api.v1.soap.ProductServiceSOAPEndPoint;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductRepository {
    private final ProductServiceSOAPEndPoint prdSvc;

    public List<Rule> findProductFeeRulesByProductId(Long productId) {
        return fakeRules();
        // return prdSvc.findProductFeeRulesByProductId(productId);
    }

    private List<Rule> fakeRules() {
        // conditions
        // date based conditions
        Condition aug = new LocalDateRangeCondition("pricingDt", LocalDate.of(2019, 8, 1), LocalDate.of(2019, 8, 31));

        // time based conditions
        Condition am = new LocalTimeRangeCondition("pricingTime", LocalTime.of(8, 0, 0), LocalTime.of(12, 0, 0));
        Condition pm = new LocalTimeRangeCondition("pricingTime", LocalTime.of(12, 0, 0), LocalTime.of(18, 0, 0));

        // weekday condition
        Condition mon = new SimpleCondition("weekday", "Mon");
        Condition fri = new SimpleCondition("weekday", "Fri");

        // age based conditions
        Condition junior = new LongRangeCondition("age", 10L, 20L);
        Condition senior = new LongRangeCondition("age", 60L, 100L);

        // residency conditions
        Condition residency = new SimpleCondition("residency", "true");

        // rule result which is the fee
        Fee julFee = new Fee();
        julFee.setAmount(BigDecimal.ONE);

        Fee augFee = new Fee();
        augFee.setAmount(BigDecimal.TEN);

        // rules
        Rule augMonMorningJuniorResidency = new ProductPriceRule()
                .setName("aug mon am junior residency")
                .setPriority(1)
                .given(ConditionGroup.all(aug, am, mon, junior, residency))
                .then(julFee);

        Rule augMonMorningJuniorNonResidency = new ProductPriceRule()
                .setName("aug mon am junior non residency")
                .setPriority(2)
                .given(ConditionGroup.all(aug, am, mon, junior, residency.reverse()))
                .then(augFee);

        Rule augMonMorningSeniorResidency = new ProductPriceRule()
                .setName("aug mon am senior residency")
                .setPriority(2)
                .given(ConditionGroup.all(aug, am, mon, senior, residency))
                .then(augFee);

        Rule augMonMorningSeniorNonResidency = new ProductPriceRule()
                .setName("aug mon am senior non residency")
                .setPriority(2)
                .given(ConditionGroup.all(aug, am, mon, senior, residency.reverse()))
                .then(augFee);

        Rule augMonMorningOthersNonResidency = new ProductPriceRule()
                .setName("aug mon am senior non residency")
                .setPriority(2)
                .given(ConditionGroup.all(aug, am, mon, ConditionGroup.any(junior, senior).reverse(), residency.reverse()))
                .then(augFee);

        return Arrays.asList(augMonMorningJuniorResidency,
                augMonMorningJuniorNonResidency,
                augMonMorningSeniorResidency,
                augMonMorningSeniorNonResidency,
                augMonMorningOthersNonResidency);
    }
}
