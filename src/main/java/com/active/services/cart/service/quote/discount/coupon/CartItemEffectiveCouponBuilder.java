package com.active.services.cart.service.quote.discount.coupon;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.CouponMode;
import com.active.services.cart.service.quote.discount.condition.DiscountSequentialSpecs;
import com.active.services.cart.service.quote.discount.condition.DiscountSpecification;
import com.active.services.cart.service.quote.discount.condition.NotExpiredSpec;
import com.active.services.domain.DateTime;
import com.active.services.product.Discount;
import com.google.common.base.Functions;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.builder.Builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toMap;
import static org.apache.commons.collections4.SetUtils.emptyIfNull;

public class CartItemEffectiveCouponBuilder implements Builder<Optional<CartItemCoupons>> {

    private CartItemCoupons cartItemCoupons;

    public CartItemEffectiveCouponBuilder cartItemCoupons(CartItemCoupons cartItemCoupons) {
        this.cartItemCoupons = cartItemCoupons;

        return this;
    }

    @Override
    public Optional<CartItemCoupons> build() {
        List<Discount> results = new ArrayList<>();

        Map<String, Discount> effectiveCouponsByCode = cartItemCoupons.getCouponDiscounts().stream()
            .filter(discount -> {
                DiscountSpecification spec = DiscountSequentialSpecs.allOf(
                    new NotExpiredSpec(discount.getStartDate(), discount.getEndDate(), new DateTime(LocalDateTime.now()))
//                    new UsageLimitSpec(productRepo, context.getCart().getId(), cartItem.getId(), cartItem.getQuantity(), discount),
//                    new UniqueUsedSpec(discount.getId(), context.getUsedUniqueCouponDiscountsIds())
            );

            return spec.satisfy();
        }).collect(toMap(Discount::getCouponCode, Functions.identity()));

        CartItem cartItem = cartItemCoupons.getCartItem();
        if (cartItem.getCouponMode() != null &&
            cartItem.getCouponMode() == CouponMode.HIGH_PRIORITY) {
            emptyIfNull(cartItem.getCouponCodes()).forEach(cCode -> {
                Discount effectiveCoupon = effectiveCouponsByCode.get(cCode);
                results.add(effectiveCoupon);
            });
        }

        // Line level does not override
        if (CollectionUtils.isEmpty(results)) {
            results.addAll(effectiveCouponsByCode.values());
        }

        if (CollectionUtils.isEmpty(results)) {
            return Optional.empty();
        }

        return Optional.of(CartItemCoupons.builder().cartItem(cartItem).couponDiscounts(results).build());
    }
}
