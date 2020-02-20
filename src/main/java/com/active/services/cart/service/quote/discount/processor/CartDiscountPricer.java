package com.active.services.cart.service.quote.discount.processor;

import com.active.services.cart.model.DiscountType;
import com.active.services.cart.service.quote.CartPricer;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.coupon.CouponDiscountHandler;
import com.active.services.cart.service.quote.discount.domain.CartItemDiscounts;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

@RequiredArgsConstructor
public class CartDiscountPricer implements CartPricer {

    @NonNull private final DiscountLoader loader;
    @NonNull private final DiscountType type;

    @Override
    public void quote(CartQuoteContext context) {

        List<CartItemDiscounts> cartItemCoupons = loader.load();
        if (CollectionUtils.isEmpty(cartItemCoupons)) {
            return;
        }
        cartItemCoupons.stream()
                .forEach(cartItemDisc -> new CartItemDiscountPricer(getDiscountHandler(context, cartItemDisc))
                        .quote(context, cartItemDisc.getCartItem()));
    }

    private DiscountHandler getDiscountHandler(CartQuoteContext context, CartItemDiscounts cartItemDiscounts) {
        if (type == DiscountType.COUPON_CODE) {
            return new CouponDiscountHandler(context, cartItemDiscounts);
        }
        return new CouponDiscountHandler(context, cartItemDiscounts);
    }

}
