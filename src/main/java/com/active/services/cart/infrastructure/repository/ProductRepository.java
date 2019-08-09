package com.active.services.cart.infrastructure.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import com.active.services.cart.domain.rule.Action;
import com.active.services.cart.domain.rule.BaseRule;
import com.active.services.cart.domain.rule.Condition;
import com.active.services.cart.domain.rule.ConditionGroup;
import com.active.services.cart.domain.rule.Rule;
import com.active.services.cart.domain.rule.fee.LocalDateRangeCondition;
import com.active.services.cart.domain.rule.fee.LocalTimeRangeCondition;
import com.active.services.cart.domain.rule.fee.LongRangeCondition;
import com.active.services.cart.domain.rule.fee.SimpleCondition;
import com.active.services.cart.domain.rule.product.ProductFact;
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
        Fee augFee = new Fee();
        augFee.setAmount(BigDecimal.TEN);
        Action<ProductFact> setAugFee = fact -> fact.setResult(augFee);

        // rules
        Rule<ProductFact> augMonMorningJuniorResidency = new BaseRule<ProductFact>()
                .setName("aug mon am junior residency")
                .setPriority(1)
                .given(ConditionGroup.all(aug, am, mon, junior, residency))
                .then(setAugFee);

        Rule<ProductFact> augMonMorningJuniorNonResidency = new BaseRule<ProductFact>()
                .setName("aug mon am junior non residency")
                .setPriority(2)
                .given(ConditionGroup.all(aug, am, mon, junior, residency.reverse()))
                .then(setAugFee);

        Rule<ProductFact> augMonMorningSeniorResidency = new BaseRule<ProductFact>()
                .setName("aug mon am senior residency")
                .setPriority(2)
                .given(ConditionGroup.all(aug, am, mon, senior, residency))
                .then(setAugFee);

        Rule<ProductFact> augMonMorningSeniorNonResidency = new BaseRule<ProductFact>()
                .setName("aug mon am senior non residency")
                .setPriority(2)
                .given(ConditionGroup.all(aug, am, mon, senior, residency.reverse()))
                .then(setAugFee);

        Rule<ProductFact> augMonMorningOthersNonResidency = new BaseRule<ProductFact>()
                .setName("aug mon am other non residency")
                .setPriority(2)
                .given(ConditionGroup.all(aug, am, mon, ConditionGroup.any(junior, senior).reverse(), residency.reverse()))
                .then(setAugFee);

        Rule<ProductFact> augMonMorningOthersResidency = new BaseRule<ProductFact>()
                .setName("aug mon am other residency")
                .setPriority(2)
                .given(ConditionGroup.all(aug, am, mon, ConditionGroup.any(junior, senior).reverse(), residency))
                .then(setAugFee);

        Rule<ProductFact> augMonPMJuniorResidency = new BaseRule<ProductFact>()
                .setName("aug mon pm junior residency")
                .setPriority(1)
                .given(ConditionGroup.all(aug, pm, mon, junior, residency))
                .then(setAugFee);

        Rule<ProductFact> augMonPMJuniorNonResidency = new BaseRule<ProductFact>()
                .setName("aug mon pm junior non residency")
                .setPriority(2)
                .given(ConditionGroup.all(aug, pm, mon, junior, residency.reverse()))
                .then(setAugFee);

        Rule<ProductFact> augMonPMSeniorResidency = new BaseRule<ProductFact>()
                .setName("aug mon pm senior residency")
                .setPriority(2)
                .given(ConditionGroup.all(aug, pm, mon, senior, residency))
                .then(setAugFee);

        Rule<ProductFact> augMonPMSeniorNonResidency = new BaseRule<ProductFact>()
                .setName("aug mon pm senior non residency")
                .setPriority(2)
                .given(ConditionGroup.all(aug, pm, mon, senior, residency.reverse()))
                .then(setAugFee);

        Rule<ProductFact> augMonPMOthersNonResidency = new BaseRule<ProductFact>()
                .setName("aug mon pm other non residency")
                .setPriority(2)
                .given(ConditionGroup.all(aug, pm, mon, ConditionGroup.any(junior, senior).reverse(),
                        residency.reverse()))
                .then(setAugFee);

        Rule<ProductFact> augMonPMOthersResidency = new BaseRule<ProductFact>()
                .setName("aug mon pm other residency")
                .setPriority(2)
                .given(ConditionGroup.all(aug, pm, mon, ConditionGroup.any(junior, senior).reverse(), residency))
                .then(setAugFee);

        return Arrays.asList(augMonMorningJuniorResidency,
                augMonMorningJuniorNonResidency,
                augMonMorningSeniorResidency,
                augMonMorningSeniorNonResidency,
                augMonMorningOthersNonResidency);
    }
}
