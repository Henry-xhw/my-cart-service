package com.active.services.cart.infrastructure.drools;

import lombok.extern.slf4j.Slf4j;
import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.kie.api.event.rule.RuleRuntimeEventListener;


@Slf4j
public class DroolsDebugRuntimeEventListener implements RuleRuntimeEventListener {
    @Override
    public void objectInserted(ObjectInsertedEvent event) {
        LOG.debug("{}", event);
    }

    @Override
    public void objectUpdated(ObjectUpdatedEvent event) {
        LOG.debug("{}", event);
    }

    @Override
    public void objectDeleted(ObjectDeletedEvent event) {
        LOG.debug("{}", event);
    }
}
