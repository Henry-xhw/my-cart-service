package com.active.services.cart.application.impl;

import com.active.services.cart.application.CartItemSelector;
import com.active.services.cart.client.soap.ProductServiceSoap;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.CartItemDiscountsApplication;
import com.active.services.cart.service.quote.discount.Discount;
import com.active.services.cart.service.quote.discount.algorithm.DiscountsAlgorithms;
import com.active.services.cart.service.quote.discount.condition.DiscountSpecs;
import com.active.services.domain.DateTime;
import com.active.services.order.discount.membership.MembershipDiscountsHistory;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MembershipDiscountEngine {
    private final CartItemSelector nonMembershipCartItemSelector;
    private final ProductServiceSoap productRepo;
    private final DiscountSpecs discountSpecs;

    public void apply(CartQuoteContext context) {
        List<CartItem> noMembershipItems = nonMembershipCartItemSelector.select(context.getCart());

        List<Long> productIds = noMembershipItems.stream()
                .map(CartItem::getProductId)
                .collect(Collectors.toList());

        Map<Long, List<MembershipDiscountsHistory>> membershipDiscounts = productRepo.findMembershipDiscounts(productIds);

        for (CartItem it : noMembershipItems) {
            if (!membershipDiscounts.containsKey(it.getProductId()) ||
                    CollectionUtils.isEmpty(membershipDiscounts.get(it.getProductId()))) {
                continue;
            }

            List<Discount> discounts = membershipDiscounts.get(it.getProductId()).stream()
                    .map(m -> new Discount(m.getName(), m.getDescription(), m.getAmount(), m.getAmountType())
                            .setCondition(discountSpecs
                                    .membershipDiscount(m.getMembershipId(), it.getPersonIdentifier(), context.getCart(),
                                            new DateTime(LocalDateTime.now()), m)))
                    .collect(Collectors.toList());


            new CartItemDiscountsApplication(context, it, discounts, DiscountsAlgorithms.bestAlgorithm(it, context.getCurrency()))
                    .apply();

        }
    }
}
