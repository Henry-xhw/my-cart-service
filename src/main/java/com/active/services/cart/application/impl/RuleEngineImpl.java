package com.active.services.cart.application.impl;

import com.active.services.cart.application.RuleEngine;

import com.active.services.cart.infrastructure.drools.DroolsAgendaEventListener;
import com.active.services.cart.infrastructure.drools.DroolsDebugRuntimeEventListener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RuleEngineImpl implements RuleEngine {
    @NonNull
    private final KieContainer kieContainer;

    @Override
    public void runRule(List<?> facts) {
        long start = 0L;
        start = System.currentTimeMillis();

        StatelessKieSession session = kieContainer.newStatelessKieSession();
        LOG.debug("Create stateless session takes: {}ms", System.currentTimeMillis() - start);
        start = System.currentTimeMillis();

        session.setGlobal("LOG", LOG);
        session.addEventListener(new DroolsAgendaEventListener());
        session.addEventListener(new DroolsDebugRuntimeEventListener());
        session.execute(facts);

        LOG.debug("Execute rule takes {}ms", System.currentTimeMillis() - start);
    }
}
