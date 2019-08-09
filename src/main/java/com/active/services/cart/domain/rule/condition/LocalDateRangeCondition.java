package com.active.services.cart.domain.rule.condition;

import java.time.LocalDate;

public class LocalDateRangeCondition extends RangeCondition<LocalDate> {
    public LocalDateRangeCondition(String field, LocalDate start, LocalDate end) {
        super(field, start, end);
    }
}
