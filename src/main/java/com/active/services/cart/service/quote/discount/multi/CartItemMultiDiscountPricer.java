package com.active.services.cart.service.quote.discount.multi;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartItemPricer;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.DiscountApplication;
import com.active.services.cart.service.quote.discount.DiscountFeeLoader;
import com.active.services.product.DiscountType;
import com.active.services.product.discount.multi.DiscountTier;
import com.active.services.product.discount.multi.MultiDiscount;

import lombok.RequiredArgsConstructor;

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

        DiscountApplication disc = DiscountApplication.builder()
                .name(multiDiscount.getName())
                .description(multiDiscount.getDescription())
                .amount(discountTier.getAmount())
                .amountType(discountTier.getAmountType())
                .discountId(multiDiscount.getId())
                .discountType(DiscountType.MULTI)
                .startDate(multiDiscount.getStartDate() != null ?
                        multiDiscount.getStartDate().toDate().toInstant() : null)
                .endDate(multiDiscount.getEndDate() != null ?
                        multiDiscount.getEndDate().toDate().toInstant() : null)
                .build();
        context.addAppliedDiscount(disc);

        new DiscountFeeLoader(context, cartItem, disc).apply();
    }
}
