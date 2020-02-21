package com.active.services.cart.service.quote.discount;

import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.condition.DiscountSequentialSpecs;
import com.active.services.cart.service.quote.discount.condition.DiscountSpecification;
import com.active.services.cart.service.quote.discount.condition.NotExpiredSpec;
import com.active.services.cart.service.quote.discount.condition.UniqueUsedSpec;
import com.active.services.domain.DateTime;
import com.active.services.product.DiscountType;

import java.time.LocalDateTime;

public class DiscountConvertor {

    public static DiscountApplication convert(com.active.services.product.Discount disc, CartQuoteContext context) {
        return DiscountApplication.builder()
                .name(disc.getName())
                .description(disc.getDescription())
                .amount(disc.getAmount())
                .amountType(disc.getAmountType())
                .discountId(disc.getId())
                .discountType(DiscountType.COUPON)
                .algorithm(disc.getDiscountAlgorithm())
                .condition(buildDiscountSpecification(context, disc)).build();
    }

    private static DiscountSpecification buildDiscountSpecification(CartQuoteContext context,
                                                                    com.active.services.product.Discount disc) {
        return DiscountSequentialSpecs.allOf(
                new NotExpiredSpec(disc.getStartDate(), disc.getEndDate(), new DateTime(LocalDateTime.now())),
                new UniqueUsedSpec(disc.getId(), context.getUsedUniqueCouponDiscountsIds())
        );
    }
}
