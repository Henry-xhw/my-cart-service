package com.active.services.cart.service.quote.discount.processor;

import com.active.services.cart.client.soap.ProductServiceSoap;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartItemPricer;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.CouponCodeDiscountHandler;
import com.active.services.cart.service.quote.discount.Discount;
import com.active.services.cart.service.quote.discount.DiscountHandler;
import com.active.services.product.DiscountType;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Slf4j
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CartItemDiscountPricer implements CartItemPricer {

    @NonNull
    private final DiscountType type;
    @NonNull
    private final ProductServiceSoap productRepo;

    @Override
    public void quote(CartQuoteContext context, CartItem cartItem) {

        DiscountHandler handler = getHandler(context, cartItem);

        List<Discount> discounts = handler.loadDiscounts();

        if (CollectionUtils.isEmpty(discounts)) {
            return;
        }

        if (cartItem.getNetPrice().compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        handler.getDiscountAlgorithm().apply(discounts).forEach(disc ->
                new DiscountFeeLoader(context, cartItem, disc).apply());
    }

    private DiscountHandler getHandler(CartQuoteContext context, CartItem cartItem) {
        if (DiscountType.COUPON == type) {
            return new CouponCodeDiscountHandler(productRepo, context, cartItem);
        }
        return null;
    }
}
