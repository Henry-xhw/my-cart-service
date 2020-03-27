package com.active.services.cart.service.quote.discount.adhoc;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.Discount;
import com.active.services.cart.service.quote.CartItemFeeBuilder;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.CartItemDiscountBasePricer;
import com.active.services.cart.service.quote.discount.DiscountAmountCalcUtil;
import com.active.services.oms.BdUtil;
import com.active.services.order.discount.OrderLineDiscountOrigin;
import com.active.services.product.AmountType;
import com.active.services.product.DiscountType;

import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

public class CartItemAdHocDiscountPricer extends CartItemDiscountBasePricer {
    @Override
    public void doQuote(CartQuoteContext context, CartItem cartItem) {
        if (CollectionUtils.isNotEmpty(cartItem.getAdHocDiscounts())) {
            cartItem.getAdHocDiscounts().stream()
                .forEach(adHocDiscount -> {
                    Discount disc = new Discount();
                    disc.setIdentifier(UUID.randomUUID());
                    disc.setName(adHocDiscount.getDiscountName() == null ?
                            "Ad-Hoc discount" : adHocDiscount.getDiscountName());
                    disc.setDescription(null);
                    disc.setAmount(adHocDiscount.getDiscountAmount());
                    disc.setAmountType(AmountType.FLAT);
                    disc.setDiscountType(DiscountType.AD_HOC);
                    disc.setDiscountId(adHocDiscount.getId());
                    disc.setCartId(context.getCart().getId());
                    disc.setDescription("Ad-Hoc discount");
                    disc.setOrigin(OrderLineDiscountOrigin.AD_HOC);
                    disc.setDiscountGroupId(adHocDiscount.getDiscountGroupId());

                    BigDecimal discAmount = DiscountAmountCalcUtil.calcFlatAmount(cartItem.getNetPrice(), disc.getAmount(),
                            disc.getAmountType(), context.getCurrency());

                    if (BdUtil.comparesToZero(discAmount)) {
                        return;
                    }
                    disc.setAmount(discAmount);
                    Discount discount = context.addAppliedDiscount(disc);
                    cartItem.getPriceCartItemFee().get()
                            .addSubItemFee(Arrays.asList(CartItemFeeBuilder.buildDiscountItemFee(discount,
                                    discAmount, 1)));
                });
        }
    }
}
