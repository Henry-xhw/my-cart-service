package com.active.services.cart.service.quote.discount;

import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.condition.DiscountSequentialSpecs;
import com.active.services.cart.service.quote.discount.condition.DiscountSpecification;
import com.active.services.cart.service.quote.discount.condition.NotExpiredSpec;
import com.active.services.cart.service.quote.discount.condition.UniqueUsedSpec;
import com.active.services.cart.service.quote.discount.domain.Discount;
import com.active.services.domain.DateTime;
import com.active.services.product.DiscountType;

import java.time.LocalDateTime;

public class DiscountConvertor {

    public static Discount convert(com.active.services.product.Discount disc, CartQuoteContext context) {
        Discount discount = new Discount(disc.getName(), disc.getDescription(), disc.getAmount(),
                disc.getAmountType(), disc.getId(), DiscountType.COUPON, disc.getCouponCode(),
                disc.getDiscountAlgorithm());
        discount.setCondition(buildDiscountSpecification(context, disc));
        return discount;
    }

    private static DiscountSpecification buildDiscountSpecification(CartQuoteContext context,
                                                             com.active.services.product.Discount discount) {
        return DiscountSequentialSpecs.allOf(
                new NotExpiredSpec(discount.getStartDate(), discount.getEndDate(), new DateTime(LocalDateTime.now())),
                new UniqueUsedSpec(discount.getId(), context.getUsedUniqueCouponDiscountsIds())
        );
    }
}
