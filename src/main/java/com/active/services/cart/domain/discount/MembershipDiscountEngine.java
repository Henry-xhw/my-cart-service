package com.active.services.cart.domain.discount;

import com.active.services.cart.domain.cart.Cart;
import com.active.services.cart.domain.cart.CartItem;
import com.active.services.cart.infrastructure.repository.ProductRepository;
import com.active.services.order.discount.membership.MembershipDiscountsHistory;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MembershipDiscountEngine {
    private final CartItemSelector nonMembershipCartItemSelector;
    private final ProductRepository productRepo;

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
            da.setCandidates(Collections.singletonList(it));

            List<Discount> discounts = new ArrayList<>();
            for (MembershipDiscountsHistory m : membershipDiscounts.get(it.getProductId())) {
                Discount discount = new Discount();
                discount.setAmount(m.getAmount());
                discount.setAmountType(m.getAmountType());
                discount.setCondition(MembershipDiscountConditions.create(m.getMembershipId(),
                        it.getPersonIdentifier(), cart));
                discounts.add(discount);
            }
            da.setDiscounts(discounts);
        }
    }
}
