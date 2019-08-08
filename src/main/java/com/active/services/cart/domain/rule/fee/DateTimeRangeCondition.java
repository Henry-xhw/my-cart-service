package com.active.services.cart.domain.rule.fee;

import java.util.Optional;

import com.active.services.cart.domain.rule.Fact;
import com.active.services.cart.domain.rule.fee.FeeCondition;
import com.active.services.domain.DateTime;

public class DateTimeRangeCondition extends FeeCondition {
    private final DateTime start;
    private final DateTime end;

    public DateTimeRangeCondition(String field, DateTime start, DateTime end) {
        super(field);
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean test(Fact fact) {
        Optional<DateTime> datetime = fact(fact);
        return datetime.filter(dt -> (start == null || !dt.before(start)) && (end == null || dt.before(end))).isPresent();
    }
}
