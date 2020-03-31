package com.active.services.cart.service.quote.discount.condition;

import com.active.services.product.DiscountAlgorithm;

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

        UniqueSpec spec = new UniqueSpec(usedDiscountIds, discountId, DiscountAlgorithm.MOST_EXPENSIVE);
        assertTrue(spec.satisfy());

        usedDiscountIds.add(RandomUtils.nextLong());
        spec = new UniqueSpec(usedDiscountIds, discountId, DiscountAlgorithm.MOST_EXPENSIVE);
        assertTrue(spec.satisfy());

        usedDiscountIds.add(discountId);
        spec = new UniqueSpec(usedDiscountIds, discountId, DiscountAlgorithm.MOST_EXPENSIVE);
        assertFalse(spec.satisfy());
    }
}
