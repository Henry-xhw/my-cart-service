package com.active.services.cart.service.quote.discount.coupon;

import com.active.platform.concurrent.TaskRunner;
import com.active.services.cart.client.soap.SOAPClient;
import com.active.services.cart.service.quote.CartPricer;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.CartItemDiscounts;
import com.active.services.cart.service.quote.discount.processor.CartItemDiscountPricer;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CouponDiscountPricer implements CartPricer {

    private final TaskRunner taskRunner;

    private final SOAPClient soapClient;

    @Override
    public void quote(CartQuoteContext context) {

        List<CartItemDiscounts> cartItemCoupons = new CouponDiscountLoader()
                .context(context).soapClient(soapClient).taskRunner(taskRunner).load();

        if (CollectionUtils.isEmpty(cartItemCoupons)) {
            return;
        }

        cartItemCoupons.stream()
                .filter(cartItemDisc -> cartItemDisc.getNetPrice().compareTo(BigDecimal.ZERO) >= 0)
                .sorted(Comparator.comparing(CartItemDiscounts :: getNetPrice).reversed())
                .forEach(cartItemDisc -> new CartItemDiscountPricer(new CouponDiscountHandler(context, cartItemDisc))
                        .quote(context, cartItemDisc.getCartItem()));
    }
}
