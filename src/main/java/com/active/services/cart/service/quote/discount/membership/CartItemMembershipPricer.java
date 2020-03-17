package com.active.services.cart.service.quote.discount.membership;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.Discount;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.CartItemDiscountBasePricer;
import com.active.services.cart.service.quote.discount.DiscountFeeLoader;
import com.active.services.cart.service.quote.discount.algorithm.BestDiscountAlgorithm;
import com.active.services.cart.service.quote.discount.condition.DiscountSequentialSpecs;
import com.active.services.cart.service.quote.discount.condition.NotExpiredSpec;
import com.active.services.domain.DateTime;
import com.active.services.order.discount.membership.MembershipDiscountsHistory;
import com.active.services.product.DiscountType;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

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
        if (histories.isEmpty()) {
            return;
        }

        Instant now = Instant.now();
        List<Discount> discounts = histories.stream().map(history -> buildDiscount(context, history)).filter(discount -> {
            DiscountSequentialSpecs spec = DiscountSequentialSpecs.allOf(
                    new NotExpiredSpec(discount.getStartDate(), discount.getEndDate(), now));
            new MemberShipSpec(membershipDiscountContext.getNewItemMembershipIds(), cartItem,
                    discount.getMembershipId());

            return spec.satisfy();
        }).collect(toList());

        if (discounts.isEmpty()) {
            return;
        }

        new BestDiscountAlgorithm(cartItem, context.getCurrency()).apply(discounts).forEach(disc ->
                new DiscountFeeLoader(context, cartItem, disc).load());
    }

    private Discount buildDiscount(CartQuoteContext context, MembershipDiscountsHistory membershipDiscount) {
        Discount discount = new Discount();
        DateTime startDate = membershipDiscount.getStartDate();
        if (startDate != null) {
            discount.setStartDate(startDate.toDate().toInstant());
        }
        DateTime endDate = membershipDiscount.getEndDate();
        if (endDate != null) {
            discount.setEndDate(endDate.toDate().toInstant());
        }
        discount.setCartId(context.getCart().getId());
        discount.setName(membershipDiscount.getName());
        discount.setDescription(membershipDiscount.getDescription());
        discount.setDiscountId(membershipDiscount.getId());
        discount.setDiscountType(DiscountType.MEMBERSHIP);
        discount.setAmount(membershipDiscount.getAmount());
        discount.setAmountType(membershipDiscount.getAmountType());
        discount.setIdentifier(UUID.randomUUID());
        discount.setMembershipId(membershipDiscount.getMembershipId());
        return discount;
    }
}
