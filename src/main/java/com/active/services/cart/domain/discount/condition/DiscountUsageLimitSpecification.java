package com.active.services.cart.domain.discount.condition;

import com.active.services.cart.infrastructure.repository.ProductRepository;
import com.active.services.product.Discount;
import com.active.services.product.DiscountUsageCountType;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DiscountUsageLimitSpecification implements DiscountSpecification {
    private final ProductRepository productRepo;
    private final Long cartId;
    private final Long cartItemId;
    private final Integer quantity;
    private final Discount discount;

    @Override
    public boolean satisfy() {
        Long ignoreId;
        if (DiscountUsageCountType.PER_ORDER == discount.getUsageCountType()) {
            ignoreId = cartId;
        } else {
            ignoreId = cartItemId;
            // PER_ORDER_LINE and PER_ORDER_LINE_QTY need to ignore the current line
        }

        return !productRepo.isDiscountLimitReached(discount, quantity, ignoreId);
    }
}
