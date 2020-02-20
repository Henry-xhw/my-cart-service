package com.active.services.cart.service.quote.discount.processor;

import com.active.services.cart.service.quote.CartPricer;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.coupon.CouponDiscountPricer;
import com.active.services.product.DiscountType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CartDiscountPricer implements CartPricer {

    private final DiscountType type;

    @Override
    public void quote(CartQuoteContext context) {
        if (DiscountType.COUPON == type) {
            getCouponDiscountPricer().quote(context);
        }
    }

    @Lookup
    public CouponDiscountPricer getCouponDiscountPricer() {
        return null;
    }
}
