package com.active.services.cart.service.quote.discount;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.service.quote.CartItemFeeBuilder;
import com.active.services.cart.service.quote.CartQuoteContext;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class DiscountFeeLoader {

    @NonNull private final CartQuoteContext cartQuoteContext;
    @NonNull private final CartItem item;
    @NonNull private final DiscountApplication disc;

    public void apply() {

        Optional<CartItemFee> priceFeeItems = item.getPriceCartItemFee();
        if (!priceFeeItems.isPresent()) {
            return;
        }
        cartQuoteContext.addAppliedDiscount(disc);
        BigDecimal discAmount = DiscountAmountCalcUtil.calcFlatAmount(item.getNetPrice(), disc.getAmount(),
                disc.getAmountType(), cartQuoteContext.getCurrency());

        applyDiscount(priceFeeItems.get(), disc, discAmount);
    }

    private void applyDiscount(CartItemFee fee, DiscountApplication disc, BigDecimal discAmount) {
        List<CartItemFee> cartItemFees = new ArrayList<>();
        cartItemFees.add(CartItemFeeBuilder.buildDiscountItemFee(disc, discAmount, fee.getUnits()));
        fee.addSubItemFee(cartItemFees);
    }
}
