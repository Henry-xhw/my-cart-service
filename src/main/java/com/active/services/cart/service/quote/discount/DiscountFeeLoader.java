package com.active.services.cart.service.quote.discount;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.service.quote.CartItemFeeBuilder;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.domain.Discount;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class DiscountFeeLoader {

    @NonNull private final CartQuoteContext cartQuoteContext;
    @NonNull private final CartItem item;
    @NonNull private final Discount disc;

    public void apply() {
        List<CartItemFee> priceFeeItems = item.getPriceCartItemFee();
        if (CollectionUtils.isEmpty(priceFeeItems)) {
            return;
        }
        cartQuoteContext.addAppliedDiscount(disc);
        BigDecimal discAmount = disc.apply(item.getNetPrice(), cartQuoteContext.getCurrency());
        //should .multiply priceFeeItems.size())?
        item.refreshNetPriceByDiscAmt(discAmount);
        priceFeeItems.forEach(f -> applyDiscount(f, disc, discAmount));
    }

    private void applyDiscount(CartItemFee fee, Discount disc, BigDecimal discAmount) {

        List<CartItemFee> cartItemFees = new ArrayList<>();
        cartItemFees.add(CartItemFeeBuilder.buildDiscountItemFee(disc, discAmount, fee.getUnits()));

        fee.addSubItemFee(cartItemFees);
        fee.refreshUnitPriceByDiscAmt(discAmount);
    }
}
