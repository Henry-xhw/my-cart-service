package com.active.services.cart.service.quote.discount.coupon;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.Discount;
import com.active.services.product.DiscountType;
import com.active.services.product.nextgen.v1.dto.DiscountUsage;

import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

@Data
class CouponDiscountContext {

    private List<DiscountUsage> discountUsages;

    private Map<CartItem, List<com.active.services.product.Discount>> cartItemDiscountMap;

    public Optional<DiscountUsage> findDiscountUsageByDiscountId(long discountId) {
        return emptyIfNull(discountUsages).stream().filter(du -> du.getDiscountId().equals(discountId)).findAny();
    }

    public Set<Long> getLimitedDiscountIds() {
        return cartItemDiscountMap.values().stream().flatMap(List::stream)
                .filter(d -> d.getUsageLimit() == -1)
                .map(com.active.services.product.Discount::getId).collect(Collectors.toSet());
    }

    public List<Long> getUsedDiscountIds(List<Discount> appliedDiscount) {
        return CollectionUtils.emptyIfNull(appliedDiscount).stream()
                .filter(disc -> disc.getDiscountType() == DiscountType.COUPON)
                .map(Discount::getDiscountId).collect(Collectors.toList());
    }
}
