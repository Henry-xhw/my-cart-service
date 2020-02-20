package com.active.services.cart.service.quote.discount.coupon;

import com.active.platform.concurrent.TaskRunner;
import com.active.services.cart.client.soap.SOAPClient;
import com.active.services.cart.service.quote.CartPricer;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.product.Discount;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CartCouponPricer implements CartPricer {

    private final TaskRunner taskRunner;

    private final SOAPClient soapClient;

    @Override
    public void quote(CartQuoteContext context) {
        // Step1: load
/*        List<CartItemCoupons> cartItemCoupons = new CartItemCouponsLoader()
                .context(context).soapClient(soapClient).taskRunner(taskRunner).load();

        // Step2: apply rules
        cartItemCoupons = cartItemCoupons.stream().map(cartItemCoupon ->
            new CartItemEffectiveCouponBuilder().cartItemCoupons(cartItemCoupon).build())
                .filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());

        // Step3: apply most expensive algorithm
        cartItemCoupons = new MostExpensiveItemAlgorithm()
                .setCart(context.getCart()).setCartItemCoupons(cartItemCoupons).apply();

        if (CollectionUtils.isEmpty(cartItemCoupons)) {
            return;
        }

        // Step4: apply item level algorithm.
        cartItemCoupons.forEach(effectiveCartItemCoupon ->
                getCartItemCouponPricer(effectiveCartItemCoupon.getCouponDiscounts()).quote(context,
                        effectiveCartItemCoupon.getCartItem()));*/

    }

    @Lookup
    public CartItemCouponPricer getCartItemCouponPricer(List<Discount> effectiveCoupons) {
        return null;
    }
}
