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

        Discount disc = new Discount();
        disc.setName(multiDiscount.getName());
        disc.setDescription(multiDiscount.getDescription());
        disc.setAmount(discountTier.getAmount());
        disc.setAmountType(discountTier.getAmountType());
        disc.setDiscountId(multiDiscount.getId());
        disc.setDiscountType(DiscountType.MULTI);
        if (multiDiscount.getStartDate() != null) {
            disc.setStartDate(multiDiscount.getStartDate().toDate().toInstant());
        }
        if (multiDiscount.getEndDate() != null) {
            disc.setEndDate(multiDiscount.getEndDate().toDate().toInstant());
        }
        context.addAppliedDiscount(disc);

        new DiscountFeeLoader(context, cartItem, disc).apply();
    }
}
