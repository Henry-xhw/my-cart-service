package com.active.services.cart.service.quote.discount.algorithm;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.CouponMode;
import com.active.services.cart.service.quote.discount.Discount;
import com.active.services.cart.service.quote.discount.DiscountAmountCalcUtil;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@RequiredArgsConstructor
public class BestDiscountAlgorithm implements DiscountAlgorithm {
    @NonNull
    private final CartItem cartItem;
    @NonNull
    private Currency currency;

    @Override
    public List<Discount> apply(List<Discount> discounts) {
        List<Discount> cartItemLevelDiscount = new ArrayList<>();
        if (cartItem.getCouponMode() == CouponMode.HIGH_PRIORITY) {
            cartItemLevelDiscount =
                    discounts.stream().filter(discount -> cartItem.getCouponCodes().contains(discount.getCouponCode()))
                    .collect(Collectors.toList());
        }
        if (CollectionUtils.isNotEmpty(cartItemLevelDiscount)) {
            discounts = cartItemLevelDiscount;
        }
        return Collections.singletonList(Collections.max(discounts,
                comparing(disc -> DiscountAmountCalcUtil.calcFlatAmount(cartItem.getNetPrice(),
                        disc.getAmount(), disc.getAmountType(), currency))));
    }
}
