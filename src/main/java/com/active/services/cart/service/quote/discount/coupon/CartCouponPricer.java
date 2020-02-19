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
        List<CartItemEffectiveCouponBuilder> builders = new CartItemEffectiveCouponBuildersBuilder()
                .context(context).soapClient(soapClient).taskRunner(taskRunner).build();

        for (CartItemEffectiveCouponBuilder builder : builders) {
            List<Discount> effectiveCoupons = builder.build();
            getCartItemCouponPricer(effectiveCoupons).quote(context, builder.getCartItem());
        }
    }

    @Lookup
    public CartItemCouponPricer getCartItemCouponPricer(List<Discount> effectiveCoupons) {
        return null;
    }
}
