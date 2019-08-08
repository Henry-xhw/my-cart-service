package com.active.services.cart.domain.rule.fee;

import java.time.LocalDate;
import java.util.Optional;

import com.active.services.cart.domain.rule.Fact;

public class LocalDateRangeCondition extends SingleFieldCondition {
    private final LocalDate start;
    private final LocalDate end;

    public LocalDateRangeCondition(String field, LocalDate start, LocalDate end) {
        super(field);
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean test(Fact fact) {
        Optional<LocalDate> datetime = fact(fact);
        return datetime.filter(dt -> (start == null || !dt.isBefore(start)) && (end == null || dt.isBefore(end))).isPresent();
    }
}
