package com.active.services.cart.domain.rule.condition;

import java.time.LocalDate;

/**
 * this class is not needed from computation(rule execution perspective), but it might be needed from persistence
 * perspective, e.g a certain condition needs to be persisted into db, a generic class may be not.
 */
public class LocalDateRangeCondition extends RangeCondition<LocalDate> {
    public LocalDateRangeCondition(String field, LocalDate start, LocalDate end) {
        super(field, start, end);
    }
}
