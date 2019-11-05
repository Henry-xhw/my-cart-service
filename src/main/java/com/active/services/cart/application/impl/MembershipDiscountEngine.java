package com.active.services.cart.application.impl;

import com.active.services.cart.application.CartItemSelector;
import com.active.services.cart.domain.cart.Cart;
import com.active.services.cart.domain.cart.CartItem;
import com.active.services.cart.domain.discount.Discount;
import com.active.services.cart.domain.discount.DiscountApplication;
import com.active.services.cart.domain.discount.algorithm.DiscountAlgorithm;
import com.active.services.cart.domain.discount.condition.DiscountSpecifications;
import com.active.services.cart.infrastructure.repository.ProductRepository;
import com.active.services.domain.DateTime;
import com.active.services.order.discount.membership.MembershipDiscountsHistory;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MembershipDiscountEngine {
    private final CartItemSelector nonMembershipCartItemSelector;
    private final ProductRepository productRepo;
    private final DiscountAlgorithm discountAlgorithm;

    public void apply(Cart cart) {
        List<CartItem> noMembershipItems = nonMembershipCartItemSelector.select(cart);

        List<Long> productIds = noMembershipItems.stream()
                .map(CartItem::getProductId)
                .collect(Collectors.toList());

        Map<Long, List<MembershipDiscountsHistory>> membershipDiscounts = productRepo.findMembershipDiscounts(productIds);

        for (CartItem it : noMembershipItems) {
            if (!membershipDiscounts.containsKey(it.getProductId())
                    || CollectionUtils.isEmpty(membershipDiscounts.get(it.getProductId()))) {
                continue;
            }

            DiscountApplication da = new DiscountApplication();
            List<Discount> discounts = new ArrayList<>();
            for (MembershipDiscountsHistory m : membershipDiscounts.get(it.getProductId())) {
                Discount discount = new Discount(m.getName(), m.getDescription(), m.getAmount(), m.getAmountType());
                discount.setCondition(DiscountSpecifications.membershipDiscount(m.getMembershipId(),
                        it.getPersonIdentifier(), cart, new DateTime(cart.getPriceDate()), m));
                discounts.add(discount);
            }
            da.setDiscounts(discounts);

            da.apply(it, discountAlgorithm, cart.getCurrency());
        }
    }
}
