package com.active.services.cart.domain.rule;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;

import com.active.services.cart.application.RuleEngine;
import com.active.services.cart.application.impl.NativeRuleEngineImpl;
import com.active.services.cart.domain.rule.fee.ConditionGroup;
import com.active.services.cart.domain.rule.fee.LocalDateRangeCondition;
import com.active.services.cart.domain.rule.fee.LocalTimeRangeCondition;
import com.active.services.cart.domain.rule.fee.LongRangeCondition;
import com.active.services.cart.domain.rule.fee.SimpleCondition;
import com.active.services.cart.domain.rule.product.ProductFact;
import com.active.services.cart.domain.rule.product.ProductPriceRule;
import com.active.services.cart.model.KVFactPair;
import com.active.services.product.Fee;

public class ConditionalRateTestCase {
    private static List<Rule> RULES = setupRules();
    private RuleEngine ruleEngine = new NativeRuleEngineImpl();

    private static List<Rule> setupRules() {
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
                .setName("aug mon am other non residency")
                .setPriority(2)
                .given(ConditionGroup.all(aug, am, mon, ConditionGroup.any(junior, senior).reverse(), residency.reverse()))
                .then(augFee);

        Rule augMonMorningOthersResidency = new ProductPriceRule()
                .setName("aug mon am other residency")
                .setPriority(2)
                .given(ConditionGroup.all(aug, am, mon, ConditionGroup.any(junior, senior).reverse(), residency))
                .then(augFee);

        Rule augMonPMJuniorResidency = new ProductPriceRule()
                .setName("aug mon pm junior residency")
                .setPriority(1)
                .given(ConditionGroup.all(aug, pm, mon, junior, residency))
                .then(julFee);

        Rule augMonPMJuniorNonResidency = new ProductPriceRule()
                .setName("aug mon pm junior non residency")
                .setPriority(2)
                .given(ConditionGroup.all(aug, pm, mon, junior, residency.reverse()))
                .then(augFee);

        Rule augMonPMSeniorResidency = new ProductPriceRule()
                .setName("aug mon pm senior residency")
                .setPriority(2)
                .given(ConditionGroup.all(aug, pm, mon, senior, residency))
                .then(augFee);

        Rule augMonPMSeniorNonResidency = new ProductPriceRule()
                .setName("aug mon pm senior non residency")
                .setPriority(2)
                .given(ConditionGroup.all(aug, pm, mon, senior, residency.reverse()))
                .then(augFee);

        Rule augMonPMOthersNonResidency = new ProductPriceRule()
                .setName("aug mon pm other non residency")
                .setPriority(2)
                .given(ConditionGroup.all(aug, pm, mon, ConditionGroup.any(junior, senior).reverse(),
                        residency.reverse()))
                .then(augFee);

        Rule augMonPMOthersResidency = new ProductPriceRule()
                .setName("aug mon pm other residency")
                .setPriority(2)
                .given(ConditionGroup.all(aug, pm, mon, ConditionGroup.any(junior, senior).reverse(), residency))
                .then(augFee);

        return Arrays.asList(augMonMorningJuniorResidency,
                augMonMorningJuniorNonResidency,
                augMonMorningSeniorResidency,
                augMonMorningSeniorNonResidency,
                augMonMorningOthersNonResidency,
                augMonMorningOthersResidency,
                augMonPMOthersNonResidency,
                augMonPMOthersResidency,
                augMonPMJuniorResidency,
                augMonPMJuniorNonResidency,
                augMonPMSeniorResidency,
                augMonPMSeniorNonResidency);
    }

    @Test
    public void test() {
        List<KVFactPair> facts = new ArrayList<>();
        facts.add(new KVFactPair("pricingDt", LocalDate.now()));
        facts.add(new KVFactPair("pricingTime", LocalTime.now()));
        facts.add(new KVFactPair("weekday", DayOfWeek.MONDAY.getDisplayName(TextStyle.SHORT, Locale.US)));
        facts.add(new KVFactPair("age", ThreadLocalRandom.current().nextLong(1, 100)));
        facts.add(new KVFactPair("residency", String.valueOf(ThreadLocalRandom.current().nextInt(1, 2) % 2 == 0)));

        ProductFact fact = new ProductFact(facts);
        ruleEngine.runRules(RULES, fact);
        assertThat(fact.getResult()).isNotNull();
    }
}
