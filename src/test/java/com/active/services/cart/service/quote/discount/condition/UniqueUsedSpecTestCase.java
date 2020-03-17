package com.active.services.cart.service.quote.discount.condition;

import com.active.services.cart.service.quote.discount.coupon.UniqueUsedSpec;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UniqueUsedSpecTestCase {

    @Test
    public void satisfy() {
        Long discountId = RandomUtils.nextLong();
        List<Long> usedDiscountIds = new ArrayList<>();

        UniqueUsedSpec spec = new UniqueUsedSpec(discountId, usedDiscountIds);
        assertTrue(spec.satisfy());

        usedDiscountIds.add(RandomUtils.nextLong());
        spec = new UniqueUsedSpec(discountId, usedDiscountIds);
        assertTrue(spec.satisfy());

        usedDiscountIds.add(discountId);
        spec = new UniqueUsedSpec(discountId, usedDiscountIds);
        assertFalse(spec.satisfy());
    }
}
