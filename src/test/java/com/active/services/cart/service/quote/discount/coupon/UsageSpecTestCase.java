package com.active.services.cart.service.quote.discount.coupon;

import com.active.services.cart.service.quote.discount.DiscountFactory;
import com.active.services.product.nextgen.v1.dto.DiscountUsage;

import org.junit.Assert;
import org.junit.Test;

public class UsageSpecTestCase {
    private DiscountUsage discountUsage;
    private UsageSpec usageSpec;

    @Test
    public void discountUsageNotSatisfy() {
        // boundary value
        discountUsage = DiscountFactory.getDiscountUsage(1L, 10, 10);
        usageSpec = new UsageSpec(discountUsage);
        Assert.assertFalse(usageSpec.satisfy());

        discountUsage = DiscountFactory.getDiscountUsage(1L, 11, 10);
        usageSpec = new UsageSpec(discountUsage);
        Assert.assertFalse(usageSpec.satisfy());

    }

    @Test
    public void discountUsageSatisfy() {
        usageSpec = new UsageSpec(discountUsage);
        Assert.assertTrue(usageSpec.satisfy());
        discountUsage = DiscountFactory.getDiscountUsage(1L, 5, 10);
        usageSpec = new UsageSpec(discountUsage);
        Assert.assertTrue(usageSpec.satisfy());
    }

}
