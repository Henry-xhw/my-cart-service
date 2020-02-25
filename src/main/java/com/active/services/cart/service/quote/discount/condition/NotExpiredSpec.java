package com.active.services.cart.service.quote.discount.condition;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@RequiredArgsConstructor
public class NotExpiredSpec implements DiscountSpecification {
    private final Instant fromInclusive;
    private final Instant toInclusive;
    @NonNull private final Instant evaluateDt;

    @Override
    public boolean satisfy() {
        return (fromInclusive == null || !fromInclusive.isAfter(evaluateDt)) &&
                (toInclusive == null || !toInclusive.isBefore(evaluateDt));
    }
}
