package com.active.services.cart.service.quote.discount;

import com.active.services.cart.service.quote.CartPricer;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.coupon.CartCouponPricer;
import com.active.services.cart.service.quote.discount.membership.CartMembershipPricer;
import com.active.services.cart.service.quote.discount.multi.CartMultiDiscountPricer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CartDiscountPricer implements CartPricer {

    @Autowired
    private CartMultiDiscountPricer cartMultiDiscountPricer;

    @Override
    public void quote(CartQuoteContext context) {
        Arrays.asList(getCartMembershipPricer(), cartMultiDiscountPricer, getCartCouponPricer())
                .forEach(cartPricer -> cartPricer.quote(context));
    }

    @Lookup
    public CartMembershipPricer getCartMembershipPricer() {
        return null;
    }

    @Lookup
    public CartCouponPricer getCartCouponPricer() {
        return null;
    }
}
