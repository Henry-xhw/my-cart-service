package com.active.services.cart.service.quote.discount;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.service.quote.CartItemFeeBuilder;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.domain.Discount;

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
    @NonNull private final Discount disc;

    public void apply() {
        Optional<CartItemFee> priceFeeItems = item.getPriceCartItemFee();
        if (!priceFeeItems.isPresent()) {
            return;
        }
        cartQuoteContext.addAppliedDiscount(disc);
        BigDecimal discAmount = disc.apply(item.getNetPrice(), cartQuoteContext.getCurrency());
        item.refreshNetPriceByDiscAmt(discAmount); // cart item net price
        applyDiscount(priceFeeItems.get(), disc, discAmount);
    }

    private void applyDiscount(CartItemFee fee, Discount disc, BigDecimal discAmount) {
        List<CartItemFee> cartItemFees = new ArrayList<>();
        cartItemFees.add(CartItemFeeBuilder.buildDiscountItemFee(disc, discAmount, fee.getUnits()));
        fee.addSubItemFee(cartItemFees);
        fee.refreshUnitPriceByDiscAmt(discAmount); //do we need to refresh unit price of price item fee.
    }
}
