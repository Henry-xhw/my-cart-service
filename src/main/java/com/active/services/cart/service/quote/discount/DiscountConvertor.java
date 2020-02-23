package com.active.services.cart.service.quote.discount;

import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.condition.DiscountSequentialSpecs;
import com.active.services.cart.service.quote.discount.condition.DiscountSpecification;
import com.active.services.cart.service.quote.discount.condition.NotExpiredSpec;
import com.active.services.cart.service.quote.discount.condition.UniqueUsedSpec;
import com.active.services.product.DiscountType;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class DiscountConvertor {

    public static DiscountApplication convert(com.active.services.product.Discount disc, CartQuoteContext context) {
        return DiscountApplication.builder()
                .name(disc.getName())
                .description(disc.getDescription())
                .amount(disc.getAmount())
                .amountType(disc.getAmountType())
                .discountId(disc.getId())
                .discountType(DiscountType.COUPON)
                .couponCode(disc.getCouponCode())
                .algorithm(disc.getDiscountAlgorithm())
                .applyToRecurringBilling(disc.getApplyToRecurringBilling())
                .identifier(UUID.randomUUID())
                .cartId(context.getCart().getId())
                .condition(buildDiscountSpecification(context, disc)).build();
    }

    private static DiscountSpecification buildDiscountSpecification(CartQuoteContext context,
                                                                    com.active.services.product.Discount disc) {
        return DiscountSequentialSpecs.allOf(
                new NotExpiredSpec(Optional.ofNullable(disc.getStartDate()).map(dt -> dt.toDate().toInstant()).orElse(null),
                        Optional.ofNullable(disc.getEndDate()).map(dt -> dt.toDate().toInstant()).orElse(null),
                        Instant.now()),
                new UniqueUsedSpec(disc.getId(), context.getUsedUniqueCouponDiscountsIds())
        );
    }
}
