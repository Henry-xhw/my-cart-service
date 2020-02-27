package com.active.services.cart.service.quote.discount.multi.loader;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Data
@RequiredArgsConstructor
class MultiDiscountKey {
    private final Long productId;

    private final Instant businessDate;
}
