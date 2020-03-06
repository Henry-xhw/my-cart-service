package com.active.services.cart.service.quote.discount.multi;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.Discount;
import com.active.services.cart.service.quote.CartItemPricer;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.DiscountFeeLoader;
import com.active.services.product.DiscountType;
import com.active.services.product.discount.multi.DiscountTier;
import com.active.services.product.discount.multi.MultiDiscount;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

import static com.active.services.oms.BdUtil.comparesToZero;

@RequiredArgsConstructor
public class CartItemMultiDiscountPricer implements CartItemPricer {
    private final DiscountTier discountTier;

    private final MultiDiscount multiDiscount;

    @Override
    public void quote(CartQuoteContext context, CartItem cartItem) {
        if (comparesToZero(discountTier.getAmount())) {
            return;
        }

        if (comparesToZero(cartItem.getNetAmount())) {
            return;
        }

        Discount.DiscountBuilder discB = Discount.builder()
                .name(multiDiscount.getName())
                .description(multiDiscount.getDescription())
                .amount(discountTier.getAmount())
                .amountType(discountTier.getAmountType())
                .discountId(multiDiscount.getId())
                .discountType(DiscountType.MULTI);
        if (multiDiscount.getStartDate() != null) {
            discB.startDate(multiDiscount.getStartDate().toDate().toInstant());
        }
        if (multiDiscount.getEndDate() != null) {
            discB.endDate(multiDiscount.getEndDate().toDate().toInstant());
        }
        Discount disc = discB.build();
        disc.setIdentifier(UUID.randomUUID());
        context.addAppliedDiscount(disc);

        new DiscountFeeLoader(context, cartItem, disc).apply();
    }
}
