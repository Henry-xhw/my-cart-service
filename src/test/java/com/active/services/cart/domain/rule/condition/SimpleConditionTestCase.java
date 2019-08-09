package com.active.services.cart.domain.rule.condition;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.active.services.cart.domain.rule.Condition;
import com.active.services.cart.domain.rule.Fact;

public class SimpleConditionTestCase {

    @Test
    public void simpleConditionStringEquals() {
        Condition aTrue = new SimpleCondition<>("boolean", "true");

        Fact fact = new Fact() {
            @Override
            public <T> T getFact(String key) {
                return (T) "true";
            }
        };
        assertThat(aTrue.satisfy(fact)).isTrue();
        assertThat(aTrue.reverse().satisfy(fact)).isFalse();
    }

    @Test
    public void simpleConditionStringNotEquals() {
        Condition aFalse = new SimpleCondition<>("boolean", "false");

        Fact fact = new Fact() {
            @Override
            public <T> T getFact(String key) {
                return (T) "true";
            }
        };
        assertThat(aFalse.satisfy(fact)).isFalse();
        assertThat(aFalse.reverse().satisfy(fact)).isTrue();
    }
}
