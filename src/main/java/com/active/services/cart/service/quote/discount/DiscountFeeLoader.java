package com.active.services.cart.service.quote.discount;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.domain.Discount;
import com.active.services.cart.service.quote.CartItemFeeBuilder;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.oms.BdUtil;
import com.active.services.order.discount.OrderLineDiscountOrigin;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

@Data
@RequiredArgsConstructor
public class DiscountFeeLoader {
    @NonNull
    private final CartQuoteContext cartQuoteContext;
    @NonNull
    private final CartItem item;
    @NonNull
    private final Discount disc;

    public void load() {
        Optional<CartItemFee> priceFeeItems = item.getPriceCartItemFee();
        if (!priceFeeItems.isPresent()) {
            return;
        }

        BigDecimal discAmount = DiscountAmountCalcUtil.calcFlatAmount(item.getNetPrice(), disc.getAmount(),
                disc.getAmountType(), cartQuoteContext.getCurrency());

        if (BdUtil.comparesToZero(discAmount)) {
            return;
        }

        Discount discount = cartQuoteContext.addAppliedDiscount(disc);
        CartItemFee discountFee = CartItemFeeBuilder.buildDiscountItemFee(discount, discAmount,
                priceFeeItems.get().getUnits());
        priceFeeItems.get().addSubItemFee(Arrays.asList(discountFee));
    }
}
