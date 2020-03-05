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

        Discount discount = Discount.builder()
                .name(AA_ORDER_LINE_DISC_NAME)
                .description(AA_ORDER_LINE_DISC_DESC)
                .discountId(aaDiscount.getId())
                .discountType(DiscountType.ACTIVE_ADVANTAGE)
                .cartId(context.getCart().getId())
                .amount(discountAmt)
                .amountType(aaDiscount.getAmountType())
                .build();
        discount.setIdentifier(UUID.randomUUID());
        applyDiscount(context, cartItem.getPriceCartItemFee().get(), discount, discountAmt, 1);
    }
}
