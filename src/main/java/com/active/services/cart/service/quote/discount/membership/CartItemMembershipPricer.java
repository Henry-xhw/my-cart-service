package com.active.services.cart.service.quote.discount.membership;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.Discount;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.CartItemDiscountBasePricer;
import com.active.services.cart.service.quote.discount.DiscountFeeLoader;
import com.active.services.cart.service.quote.discount.DiscountMapper;
import com.active.services.cart.service.quote.discount.algorithm.BestDiscountAlgorithm;
import com.active.services.cart.service.quote.discount.condition.DiscountSequentialSpecs;
import com.active.services.cart.service.quote.discount.condition.NotExpiredSpec;
import com.active.services.order.discount.membership.MembershipDiscountsHistory;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CartItemMembershipPricer extends CartItemDiscountBasePricer {

    private final MembershipDiscountContext membershipDiscountContext;

    @Override
    protected void doQuote(CartQuoteContext context, CartItem cartItem) {

        List<MembershipDiscountsHistory> histories =
                membershipDiscountContext.getMembershipDiscountsHistory(cartItem.getProductId());
        if (CollectionUtils.isEmpty(histories)) {
            return;
        }

        List<Discount> discounts = histories.stream().map(history -> DiscountMapper.MAPPER.toDiscount(history, context))
                .filter(discount -> {
                    DiscountSequentialSpecs spec = DiscountSequentialSpecs.allOf(
                            new NotExpiredSpec(discount.getStartDate(), discount.getEndDate(), Instant.now()),
                            new MemberShipSpec(membershipDiscountContext.getNewItemMembershipIds(), cartItem,
                                    discount.getMembershipId()));
                    return spec.satisfy();
                }).collect(toList());

        if (discounts.isEmpty()) {
            return;
        }
        new BestDiscountAlgorithm(cartItem, context.getCurrency()).apply(discounts).forEach(disc ->
                new DiscountFeeLoader(context, cartItem, disc).load());
    }

}
