package com.active.services.cart.service.quote.discount.processor;

import com.active.services.cart.client.soap.ProductServiceSoap;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartItemPricer;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.CartItemDiscountsApplication;
import com.active.services.cart.service.quote.discount.Discount;
import com.active.services.cart.service.quote.discount.algorithm.DiscountsAlgorithms;
import com.active.services.cart.service.quote.discount.condition.DiscountSpecs;
import com.active.services.domain.DateTime;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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
    @NonNull
    private final ProductServiceSoap productRepo;

    @Override
    public void quote(CartQuoteContext context, CartItem cartItem) {

        List<String> couponCodes = context.getCartLevelCouponCodes();

        // add cartItem couponCodes.

        List<com.active.services.product.Discount> couponDiscs = getDiscount(cartItem.getProductId(), couponCodes);
        if (CollectionUtils.isEmpty(couponDiscs)) {
            return;
        }
        List<Discount> discounts = new ArrayList<>(couponDiscs.size());

        for (com.active.services.product.Discount disc : couponDiscs) {
            Discount discount = new Discount(disc.getName(), disc.getDescription(), disc.getAmount(), disc.getAmountType());
            discount.setCondition(specs.couponDiscount(disc, disc.getCouponCode(), new DateTime(LocalDateTime.now()), null,
                    context, cartItem));
            discounts.add(discount);
        }
        new CartItemDiscountsApplication(cartItem, discounts,
                DiscountsAlgorithms.getAlgorithm(context.getDiscountModel(cartItem.getProductId())),
                context.getCart().getCurrencyCode()).apply();
    }

    private List<com.active.services.product.Discount> getDiscount(Long productId, List<String> couponCodes) {
        return productRepo.findDiscountByProductIdAndCode(productId, couponCodes);
    }
}
