package com.active.services.cart.infrastructure.drools;

import lombok.extern.slf4j.Slf4j;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.event.rule.AgendaGroupPoppedEvent;
import org.kie.api.event.rule.AgendaGroupPushedEvent;
import org.kie.api.event.rule.BeforeMatchFiredEvent;
import org.kie.api.event.rule.MatchCancelledEvent;
import org.kie.api.event.rule.MatchCreatedEvent;
import org.kie.api.event.rule.RuleFlowGroupActivatedEvent;
import org.kie.api.event.rule.RuleFlowGroupDeactivatedEvent;


@Slf4j
public class DroolsAgendaEventListener implements AgendaEventListener {
    @Override
    public void matchCreated(MatchCreatedEvent event) {
        LOG.debug("matchCreated: {}", event);
    }

    @Override
    public void matchCancelled(MatchCancelledEvent event) {
        LOG.debug("matchCancelled: {}", event);
    }

    @Override
    public void beforeMatchFired(BeforeMatchFiredEvent event) {
        LOG.debug("beforeMatchFired: {}", event);
    }

    @Override
    public void afterMatchFired(AfterMatchFiredEvent event) {
        LOG.debug("afterMatchFired: {}", event);
    }

    @Override
    public void agendaGroupPopped(AgendaGroupPoppedEvent event) {
        LOG.debug("agendaGroupPopped: {}", event);
    }

    @Override
    public void agendaGroupPushed(AgendaGroupPushedEvent event) {
        LOG.debug("agendaGroupPushed: {}", event);
    }

    @Override
    public void beforeRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {
        LOG.debug("beforeRuleFlowGroupActivated: {}", event);
    }

    @Override
    public void afterRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {
        LOG.debug("afterRuleFlowGroupActivated: {}", event);
    }

    @Override
    public void beforeRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {
        LOG.debug("beforeRuleFlowGroupDeactivated: {}", event);
    }

    @Override
    public void afterRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {
        LOG.debug("afterRuleFlowGroupDeactivated{}", event);
    }
}
