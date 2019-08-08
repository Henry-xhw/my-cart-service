package com.active.services.cart.domain.rule.fee;

import java.time.LocalTime;
import java.util.Optional;

import com.active.services.cart.domain.rule.Fact;

public class LocalTimeRangeCondition extends SingleFieldCondition {
    private final LocalTime start;
    private final LocalTime end;

    public LocalTimeRangeCondition(String field, LocalTime start, LocalTime end) {
        super(field);
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean test(Fact fact) {
        Optional<LocalTime> datetime = fact(fact);
        return datetime.filter(dt -> (start == null || !dt.isBefore(start)) && (end == null || dt.isBefore(end))).isPresent();
    }
}
