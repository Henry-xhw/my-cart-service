package com.active.services.cart;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.drools.template.DataProvider;
import org.drools.template.DataProviderCompiler;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;

import com.active.services.cart.application.impl.NativeRuleEngineImpl;
import com.active.services.cart.domain.rule.Fact;
import com.active.services.cart.domain.rule.Rule;
import com.active.services.cart.domain.rule.fee.DateTimeRangeCondition;
import com.active.services.cart.domain.rule.product.ProductFact;
import com.active.services.cart.domain.rule.product.ProductPriceFactRuleProvider;
import com.active.services.cart.domain.rule.product.ProductPriceRule;
import com.active.services.domain.DateTime;
import com.active.services.product.Fee;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DroolsPriceRuleDemo {
    private NativeRuleEngineImpl ruleEngine = new NativeRuleEngineImpl();

    public static void main(String[] args) {
        // fact
        Fact productFact = ProductFact.builder()
                .pricingDt(new DateTime(LocalDateTime.now())).build();

        // time based conditions
        DateTimeRangeCondition jul = new DateTimeRangeCondition("pricingDt", new DateTime(LocalDateTime.of(2019, 7, 1
                , 0, 0)), new DateTime(LocalDateTime.of(2019, 7, 31, 0, 0)));
        DateTimeRangeCondition aug = new DateTimeRangeCondition("pricingDt", new DateTime(LocalDateTime.of(2019, 8, 1
                , 0, 0)), new DateTime(LocalDateTime.of(2019, 8, 31, 0, 0)));

        // rule result which is the fee
        Fee julFee = new Fee();
        julFee.setAmount(BigDecimal.ONE);

        Fee augFee = new Fee();
        augFee.setAmount(BigDecimal.TEN);

        // rule
        Rule janRule = new ProductPriceRule()
                .setName("jul rule")
                .setPriority(1)
                .given(jul)
                .then(julFee);

        Rule febRule = new ProductPriceRule()
                .setName("aug rule")
                .setPriority(2)
                .given(aug)
                .then(augFee);

        // run rules
        List<Rule> rules = Arrays.asList(janRule, febRule);

        DroolsPriceRuleDemo demo = new DroolsPriceRuleDemo();
        demo.runByRuleTemplate(rules, productFact);
        demo.runByNativeRuleEngine(rules, productFact);
    }

    private void runByNativeRuleEngine(List<Rule> rules, Fact fact) {
        ruleEngine.runRules(rules, fact);
    }

    private void runByRuleTemplate(List<Rule> rules, Fact fact) {
        ProductPriceFactRuleProvider dataProvider = new ProductPriceFactRuleProvider(ProductFact.class, rules.iterator());
        String drl = compileTemplate(dataProvider);
        LOG.info("{}", drl);

        runRule(drl, fact, rules);
    }

    private void runRule(String drl, Fact fact, List<Rule> rules) {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write("src/main/resources/com/active/services/platform/cart/domain/rule/rule.drl", drl);
        kieServices.newKieBuilder(kieFileSystem).buildAll();

        KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
        StatelessKieSession statelessKieSession = kieContainer.getKieBase().newStatelessKieSession();

        statelessKieSession.setGlobal("LOG", LOG);
        List<Object> facts = new ArrayList<>(rules);
        facts.add(fact);
        statelessKieSession.execute(facts);
    }

    private String compileTemplate(DataProvider dp) {
        DataProviderCompiler dataProviderCompiler = new DataProviderCompiler();

        return dataProviderCompiler.compile(dp,
                Objects.requireNonNull(Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream("com/active/services/cart/domain/rule/product_price_template.drt")));
    }
}
