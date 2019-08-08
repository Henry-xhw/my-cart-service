package com.active.services.cart.application.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.drools.template.DataProvider;
import org.drools.template.DataProviderCompiler;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;

import com.active.services.cart.application.RuleEngine;
import com.active.services.cart.domain.rule.Fact;
import com.active.services.cart.domain.rule.Rule;
import com.active.services.cart.domain.rule.product.ProductFact;
import com.active.services.cart.domain.rule.product.ProductPriceFactRuleProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DroolsTemplateRuleEngineImpl implements RuleEngine {
    private static final String RULE_TEMPLATE = "com/active/services/cart/domain/rule/price_template.drt";
    private static final String DEFAULT_RULE = "src/main/resources/com/active/services/platform/cart/domain/rule/rule.drl";

    @Override
    public void runRule(List<?> facts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void runRules(List<Rule> rules, Fact fact) {
        ProductPriceFactRuleProvider dataProvider = new ProductPriceFactRuleProvider(ProductFact.class, rules.iterator());
        String drl = compileTemplate(dataProvider);
        LOG.info("{}", drl);

        runRule(drl, fact, rules);
    }

    private void runRule(String drl, Fact fact, List<Rule> rules) {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(DEFAULT_RULE, drl);
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
                Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(RULE_TEMPLATE)));
    }
}
