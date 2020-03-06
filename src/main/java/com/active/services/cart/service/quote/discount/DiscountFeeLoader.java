package com.active.services.cart.service.quote.discount;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.domain.Discount;
import com.active.services.cart.service.quote.CartItemFeeBuilder;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.oms.BdUtil;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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

    public void apply() {

        Optional<CartItemFee> priceFeeItems = item.getPriceCartItemFee();
        if (!priceFeeItems.isPresent()) {
            return;
        }
        BigDecimal discAmount = DiscountAmountCalcUtil.calcFlatAmount(item.getNetPrice(), disc.getAmount(),
                disc.getAmountType(), cartQuoteContext.getCurrency());
        applyDiscount(cartQuoteContext, priceFeeItems.get(), disc, discAmount, priceFeeItems.get().getUnits());
    }

    public static void applyDiscount(CartQuoteContext cartQuoteContext, CartItemFee fee, Discount disc,
                                     BigDecimal discAmount, Integer units) {
        if (!BdUtil.isGreaterThanZero(discAmount)) {
            return;
        }
        Discount appliedDiscount = cartQuoteContext.getAppliedDiscount(disc.getDiscountId(), disc.getDiscountType());
        if (appliedDiscount == null) {
            cartQuoteContext.addAppliedDiscount(disc);
            appliedDiscount = disc;
        }
        List<CartItemFee> cartItemFees = new ArrayList<>();
        cartItemFees.add(CartItemFeeBuilder.buildDiscountItemFee(appliedDiscount, discAmount, units));
        fee.addSubItemFee(cartItemFees);
    }
}
