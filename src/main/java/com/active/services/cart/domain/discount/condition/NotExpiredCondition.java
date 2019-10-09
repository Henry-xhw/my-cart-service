package com.active.services.cart.domain.discount.condition;

import com.active.services.cart.domain.discount.DiscountCondition;
import com.active.services.domain.DateTime;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotExpiredCondition implements DiscountCondition {
    private final DateTime fromInclusive;
    private final DateTime toExclusive;
    @NonNull private final DateTime evalulateDt;

    @Override
    public boolean satisfy() {
        return (fromInclusive == null || fromInclusive.before(evalulateDt)) &&
                (toExclusive == null || toExclusive.after(evalulateDt));
    }
}
