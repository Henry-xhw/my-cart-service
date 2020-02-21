package com.active.services.cart.service.quote.discount.processor;

import com.active.services.cart.client.soap.ProductServiceSoap;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartItemPricer;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.CartItemDiscountsApplication;
import com.active.services.cart.service.quote.discount.Discount;
import com.active.services.cart.service.quote.discount.algorithm.DiscountsAlgorithms;
import com.active.services.cart.service.quote.discount.condition.DiscountSpecs;
import com.active.services.product.DiscountType;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@Slf4j
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CouponDiscountPricer implements CartItemPricer {
    @NonNull
    private final DiscountSpecs specs;
    @NonNull
    private final ProductServiceSoap productRepo;

    @Override
    public void quote(CartQuoteContext context, CartItem cartItem) {

        List<com.active.services.product.Discount> couponDiscs = getDiscounts(context, cartItem);
        if (CollectionUtils.isEmpty(couponDiscs)) {
            return;
        }

        List<Discount> discounts = new ArrayList<>(couponDiscs.size());
        for (com.active.services.product.Discount disc : couponDiscs) {
            Discount discount = new Discount(disc.getName(), disc.getDescription(), disc.getAmount(),
                    disc.getAmountType(), disc.getId(), DiscountType.COUPON, disc.getCouponCode(),
                    disc.getDiscountAlgorithm());
            discount.setCondition(specs.couponDiscount(context, cartItem, disc));
            discounts.add(discount);
        }
        new CartItemDiscountsApplication(context, cartItem, discounts,
                DiscountsAlgorithms.getAlgorithm(cartItem, context.getDiscountModel(cartItem.getProductId()),
                        context.getCurrency())).apply();
    }

    private List<com.active.services.product.Discount> getDiscounts(CartQuoteContext context, CartItem cartItem) {

        Set<String> couponCodes = getCouponCodes(context, cartItem);
        if (CollectionUtils.isEmpty(couponCodes)) {
            return new ArrayList<>();
        }
        return productRepo.findDiscountsByProductIdAndCode(cartItem.getProductId(), new ArrayList<>(couponCodes));
    }

    private Set<String> getCouponCodes(CartQuoteContext quoteContext, CartItem cartItem) {

        Set<String> requestedCodes = new HashSet<>();
        Optional.ofNullable(quoteContext.getCartLevelCouponCodes()).ifPresent(requestedCodes::addAll);
        Optional.ofNullable(cartItem.getCouponCodes()).ifPresent(requestedCodes::addAll);

        return requestedCodes;
    }
}
