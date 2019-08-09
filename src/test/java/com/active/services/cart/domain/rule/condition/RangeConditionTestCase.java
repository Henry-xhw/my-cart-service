package com.active.services.cart.domain.rule.condition;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.Test;

import com.active.services.cart.domain.rule.Condition;
import com.active.services.cart.domain.rule.Fact;

public class RangeConditionTestCase {

    @Test
    public void localDateRange() {
        Condition jun = new RangeCondition<>("date", LocalDate.of(2019, 6, 1), LocalDate.of(2019, 6, 30));

        Fact fact = new Fact() {
            @Override
            public <T> T getFact(String key) {
                return (T) LocalDate.of(2019, 6, 20);
            }
        };
        assertThat(jun.satisfy(fact)).isTrue();
        assertThat(jun.reverse().satisfy(fact)).isFalse();
    }
}
