package com.active.services.cart.service.quote.discount.coupon;

import com.active.services.cart.domain.Discount;
import com.active.services.product.DiscountType;
import com.active.services.product.nextgen.v1.dto.DiscountUsage;

import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
class CouponDiscountContext {
    private List<DiscountUsage> discountUsages;

    private List<CartItemDiscounts> cartItemDiscounts;

    public Set<Long> getDiscountIds() {
        return CollectionUtils.emptyIfNull(cartItemDiscounts).stream()
                .map(cid -> cid.getAllDiscountIds())
                .flatMap(Collection::stream).distinct().collect(Collectors.toSet());
    }

    public List<Long> getUsedUniqueCouponDiscountsIds(List<Discount> appliedDiscount) {
        return CollectionUtils.emptyIfNull(appliedDiscount).stream()
                .filter(disc -> disc.getDiscountType() == DiscountType.COUPON)
                .map(Discount::getDiscountId).collect(Collectors.toList());
    }
}
