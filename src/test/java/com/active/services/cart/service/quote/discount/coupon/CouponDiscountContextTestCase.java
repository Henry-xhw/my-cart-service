package com.active.services.cart.service.quote.discount.coupon;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.Discount;
import com.active.services.product.DiscountType;
import com.active.services.product.nextgen.v1.dto.DiscountUsage;
import com.google.common.collect.Sets;

import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class CouponDiscountContextTestCase {

    @Test
    public void findDiscountUsageByDiscountIdSuccess() {
        DiscountUsage discountUsage = new DiscountUsage();
        discountUsage.setDiscountId(1L);
        CouponDiscountContext couponDiscountContext = new CouponDiscountContext();
        couponDiscountContext.setDiscountUsages(Lists.list(discountUsage));
        assertEquals(discountUsage,
                couponDiscountContext.findDiscountUsageByDiscountId(discountUsage.getDiscountId()).get());
    }

    @Test
    public void getLimitedDiscountIdsSuccess() {
        com.active.services.product.Discount discount1 = new com.active.services.product.Discount();
        discount1.setId(1L);
        discount1.setUsageLimit(1);
        com.active.services.product.Discount discount2 = new com.active.services.product.Discount();
        discount2.setId(2L);
        discount2.setUsageLimit(-1);
        Map<CartItem, List<com.active.services.product.Discount>> cartItemDiscountMap = new HashMap<>();
        cartItemDiscountMap.put(new CartItem(), Lists.list(discount1, discount2));
        CouponDiscountContext couponDiscountContext = new CouponDiscountContext();
        couponDiscountContext.setCartItemDiscountMap(cartItemDiscountMap);
        assertEquals(Sets.newHashSet(discount1.getId()), couponDiscountContext.getLimitedDiscountIds());
    }

    @Test
    public void getUsedDiscountIdsSuccess() {
        Discount discount1 = new Discount();
        discount1.setDiscountId(1L);
        discount1.setDiscountType(DiscountType.COUPON);
        Discount discount2 = new Discount();
        discount2.setDiscountId(2L);
        discount2.setDiscountType(DiscountType.AD_HOC);
        Set<Discount> appliedDiscount = Sets.newHashSet(discount1, discount2);
        assertEquals(Lists.list(discount1.getDiscountId()),
                new CouponDiscountContext().getUsedDiscountIds(appliedDiscount));
    }
}
