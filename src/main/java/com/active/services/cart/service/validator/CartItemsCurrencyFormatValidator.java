package com.active.services.cart.service.validator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.util.CollectionUtils;

import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.AdHocDiscount;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.ErrorCode;
import com.active.services.domain.currency.IllegalMonetaryValueException;
import com.active.services.oms.Money;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CartItemsCurrencyFormatValidator {

    private final String currencyCode;

    private final List<CartItem> cartItems;

    public void validate() {
        if (CollectionUtils.isEmpty(cartItems)) {
            return;
        }
        cartItems.stream().filter(Objects::nonNull).forEach(item -> {
            validateAmountCurrencyFormat(item.getOverridePrice());
            List<AdHocDiscount> adHocDiscounts = item.getAdHocDiscounts();
            if (!CollectionUtils.isEmpty(adHocDiscounts)) {
                adHocDiscounts.stream().filter(Objects::nonNull)
                    .forEach(adHocDiscount -> validateAmountCurrencyFormat(adHocDiscount.getDiscountAmount()));
            }
        });
    }

    private void validateAmountCurrencyFormat(BigDecimal amount) {
        if (Objects.isNull(amount)) {
            return;
        }
        try {
            Money.createInstance(amount, currencyCode);
        } catch (IllegalMonetaryValueException e) {
            throw new CartException(ErrorCode.VALIDATION_ERROR,
                    "Amount: {0} does not match currency {1} format.", amount, currencyCode);
        }
    }
}
