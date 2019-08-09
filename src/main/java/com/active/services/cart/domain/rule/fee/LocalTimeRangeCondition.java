package com.active.services.cart.domain.rule.fee;

import java.time.LocalTime;

public class LocalTimeRangeCondition extends RangeCondition<LocalTime> {
    public LocalTimeRangeCondition(String field, LocalTime start, LocalTime end) {
        super(field, start, end);
    }
}
