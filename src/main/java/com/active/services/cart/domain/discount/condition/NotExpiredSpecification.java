package com.active.services.cart.domain.discount.condition;

import com.active.services.domain.DateTime;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotExpiredSpecification implements DiscountSpecification {
    private final DateTime fromInclusive;
    private final DateTime toExclusive;
    @NonNull private final DateTime evaluateDt;

    @Override
    public boolean satisfy() {
        return (fromInclusive == null || fromInclusive.before(evaluateDt)) &&
                (toExclusive == null || toExclusive.after(evaluateDt));
    }
}
