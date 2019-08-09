package com.active.services.cart.domain.rule.condition;

/**
 * this class is not needed from computation(rule execution perspective), but it might be needed from persistence
 * perspective, e.g a certain condition needs to be persisted into db, a generic class may be not.
 */
public class LongRangeCondition extends RangeCondition<Long> {
    public LongRangeCondition(String field, Long start, Long end) {
        super(field, start, end);
    }
}
