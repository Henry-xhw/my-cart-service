package com.active.services.cart.service.quote.discount.condition;

import com.active.services.product.nextgen.v1.dto.DiscountUsage;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

@NonNull
@RequiredArgsConstructor
public class UsageLimitSpec implements DiscountSpecification {
    private final List<DiscountUsage> discountUsages;

    private final Long discountId;

    @Override
    public boolean satisfy() {
        return CollectionUtils.isEmpty(discountUsages) && discountUsages.stream()
                .anyMatch(discountUsage ->
                        discountId.equals(discountUsage.getDiscountId())
                        && (discountUsage.getLimit() == -1 ||
                            discountUsage.getLimit() > discountUsage.getUsage()));
    }
}
