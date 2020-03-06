package com.active.services.cart.service.quote.discount.aa;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.Discount;
import com.active.services.cart.service.quote.CartItemPricer;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.product.DiscountType;
import com.active.services.product.discount.aa.AaDiscount;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

import static com.active.services.cart.service.quote.discount.DiscountAmountCalcUtil.calcFlatAmount;
import static com.active.services.cart.service.quote.discount.DiscountFeeLoader.applyDiscount;

@RequiredArgsConstructor
public class CartItemAaDiscountPricer implements CartItemPricer {
    private static final String AA_ORDER_LINE_DISC_DESC = "ACTIVE.com Advantage member discount";

    private static final String AA_ORDER_LINE_DISC_NAME = "ACTIVE Advantage discount";

    private final AADiscountContext aaDiscountContext;

    @Override
    public void quote(CartQuoteContext context, CartItem cartItem) {

        AaDiscount aaDiscount = aaDiscountContext.getAaDiscount();

        BigDecimal discountAmt = calcFlatAmount(cartItem.getActiveProcessingFeeTotal(),
                aaDiscount.getAmount(),
                aaDiscount.getAmountType(), context.getCurrency())
                .min(aaDiscountContext.getRemainingDiscAmt());

        aaDiscountContext.subtractDiscountAmt(discountAmt);

        Discount disc = new Discount();
        disc.setIdentifier(UUID.randomUUID());
        disc.setName(AA_ORDER_LINE_DISC_NAME);
        disc.setDescription(AA_ORDER_LINE_DISC_DESC);
        disc.setAmount(discountAmt);
        disc.setAmountType(aaDiscount.getAmountType());
        disc.setDiscountId(aaDiscount.getId());
        disc.setDiscountType(DiscountType.ACTIVE_ADVANTAGE);
        disc.setCartId(context.getCart().getId());

        applyDiscount(context, cartItem.getPriceCartItemFee().get(), disc, discountAmt, 1);
    }
}
