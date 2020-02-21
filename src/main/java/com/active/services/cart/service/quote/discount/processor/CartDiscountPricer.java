package com.active.services.cart.service.quote.discount.processor;

import com.active.platform.concurrent.TaskRunner;
import com.active.services.cart.client.soap.SOAPClient;
import com.active.services.cart.model.DiscountType;
import com.active.services.cart.service.quote.CartPricer;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.DiscountHandler;
import com.active.services.cart.service.quote.discount.DiscountLoader;
import com.active.services.cart.service.quote.discount.coupon.CouponDiscountHandler;
import com.active.services.cart.service.quote.discount.coupon.CouponDiscountLoader;
import com.active.services.cart.service.quote.discount.domain.CartItemDiscounts;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import javax.ws.rs.NotSupportedException;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CartDiscountPricer implements CartPricer {

    @NonNull
    private final DiscountType type;
    @Autowired
    private SOAPClient soapClient;
    @Autowired
    private TaskRunner taskRunner;

    @Override
    public void quote(CartQuoteContext context) {

        List<CartItemDiscounts> cartItemDiscounts = getDiscountLoader(context).load();
        if (CollectionUtils.isEmpty(cartItemDiscounts)) {
            return;
        }
        cartItemDiscounts.stream()
                .forEach(cartItemDisc -> new CartItemDiscountPricer(getDiscountHandler(context, cartItemDisc))
                        .quote(context, cartItemDisc.getCartItem()));
    }

    private DiscountHandler getDiscountHandler(CartQuoteContext context, CartItemDiscounts cartItemDiscounts) {
        if (DiscountType.COUPON_CODE == type) {
            return new CouponDiscountHandler(context, cartItemDiscounts);
        }
        throw new NotSupportedException();
    }

    private DiscountLoader getDiscountLoader(CartQuoteContext context) {
        if (DiscountType.COUPON_CODE == type) {
            return CouponDiscountLoader.builder().context(context)
                    .soapClient(soapClient).taskRunner(taskRunner).build();
        }
        throw new NotSupportedException();
    }

}
