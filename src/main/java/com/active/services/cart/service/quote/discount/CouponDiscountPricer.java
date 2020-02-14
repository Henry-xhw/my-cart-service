package com.active.services.cart.service.quote.discount;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.discount.CartItemDiscountsApplication;
import com.active.services.cart.domain.discount.Discount;
import com.active.services.cart.domain.discount.algorithm.DiscountsAlgorithms;
import com.active.services.cart.domain.discount.condition.DiscountSpecs;
import com.active.services.cart.service.quote.CartItemPricer;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.domain.DateTime;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CouponDiscountPricer implements CartItemPricer {
    @NonNull
    private final DiscountSpecs specs;

    @Override
    public void quote(CartQuoteContext context, CartItem cartItem) {

        List<String> couponCodes = new ArrayList<>();
        List<com.active.services.product.Discount> couponDiscs = getDiscount(cartItem.getProductId(), couponCodes);
        List<Discount> discounts = new ArrayList<>(couponDiscs.size());

        for (com.active.services.product.Discount disc : couponDiscs) {
            Discount discount = new Discount(disc.getName(), disc.getDescription(), disc.getAmount(), disc.getAmountType());
            discount.setCondition(specs.couponDiscount(disc, disc.getCouponCode(), new DateTime(LocalDateTime.now()), null,
                    context.getCart().getId(), cartItem.getId(), cartItem.getQuantity()));
            discounts.add(discount);
        }
        new CartItemDiscountsApplication(cartItem, discounts,
                DiscountsAlgorithms.getAlgorithm(context.getDiscountModel(cartItem.getProductId())),
                context.getCart().getCurrencyCode()).apply();
    }

    private List<com.active.services.product.Discount> getDiscount(Long productId, List<String> couponDiscs) {
        return new ArrayList<>();
    }
}
