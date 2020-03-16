package com.active.services.cart.service.quote.discount.membership;

import com.active.services.cart.domain.Discount;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.CartItemDiscounts;
import com.active.services.cart.service.quote.discount.DiscountHandler;
import com.active.services.cart.service.quote.discount.algorithm.BestDiscountAlgorithm;
import com.active.services.cart.service.quote.discount.algorithm.DiscountAlgorithm;
import com.active.services.cart.service.quote.discount.condition.DiscountSequentialSpecs;
import com.active.services.cart.service.quote.discount.condition.DiscountSpecification;
import com.active.services.cart.service.quote.discount.condition.MemberShipSpec;
import com.active.services.cart.service.quote.discount.condition.NotExpiredSpec;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MemberShipDiscountHandler implements DiscountHandler {

    @NonNull
    private final CartQuoteContext context;
    @NonNull
    private final CartItemDiscounts itemDiscounts;
    @NonNull
    private final List<Long> membershipIds;

    @Override
    public List<Discount> filterDiscounts() {
        List<Discount> discounts = CollectionUtils.emptyIfNull(itemDiscounts.getDiscounts()).stream()
                .filter(discount -> satisfy(discount)).collect(Collectors.toList());
        return discounts;
    }

    private boolean satisfy(Discount discount) {
        DiscountSpecification specification = buildDiscountSpecification(discount);
        return specification.satisfy();
    }

    private DiscountSpecification buildDiscountSpecification(Discount disc) {
        return DiscountSequentialSpecs.allOf(
                new NotExpiredSpec(disc.getStartDate(), disc.getEndDate(), Instant.now()),
                new MemberShipSpec(membershipIds, itemDiscounts.getCartItem(), disc.getMembershipId())
        );
    }

    @Override
    public DiscountAlgorithm getDiscountAlgorithm() {
        return  new BestDiscountAlgorithm(itemDiscounts.getCartItem(), context.getCurrency());
    }
}
