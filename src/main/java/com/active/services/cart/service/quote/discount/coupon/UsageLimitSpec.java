package com.active.services.cart.service.quote.discount.coupon;

import com.active.services.cart.service.quote.discount.condition.DiscountSpecification;
import com.active.services.product.nextgen.v1.dto.DiscountUsage;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

@NonNull
@RequiredArgsConstructor
public class UsageLimitSpec implements DiscountSpecification {
    private final List<DiscountUsage> discountUsages;

    private final Long discountId;

    @Override
    public boolean satisfy() {
        return emptyIfNull(discountUsages).stream()
                .anyMatch(discountUsage ->
                        discountId.equals(discountUsage.getDiscountId())
                        && (discountUsage.getLimit() == -1 ||
                            discountUsage.getLimit() > discountUsage.getUsage()));
    }
}
