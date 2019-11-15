package com.active.services.cart.domain.discount;

import com.active.services.oms.BdUtil;
import com.active.services.product.AmountType;

import java.math.BigDecimal;
import java.util.Currency;

public class DiscountAmountCalcUtil {

    private DiscountAmountCalcUtil() {
    }

    public static BigDecimal calcFlatAmount(final BigDecimal toDiscount, final BigDecimal discAmt,
                                            final AmountType amtType, Currency currency) {
        BigDecimal netDiscAmt = BigDecimal.ZERO;
        if (toDiscount.compareTo(BigDecimal.ZERO) <= 0 || discAmt.compareTo(BigDecimal.ZERO) <= 0) {
            return netDiscAmt;
        }

        switch (amtType) {
            case FLAT:
                netDiscAmt = discAmt;
                break;
            case PERCENT:
                netDiscAmt = calcPercent(toDiscount, discAmt, currency);
                break;
            case FIXED_AMOUNT:
                netDiscAmt = calcFixedAmount(toDiscount, discAmt, currency);
                break;
            default:
                throw new IllegalArgumentException("amount type not supported " + amtType);
        }

        if (netDiscAmt.compareTo(toDiscount) > 0) {
            netDiscAmt = toDiscount;
        }

        return netDiscAmt;
    }

    private static BigDecimal calcPercent(final BigDecimal fee, final BigDecimal percent, Currency currency) {
        return BdUtil.setScalePerCurrency(percent.movePointLeft(2).multiply(fee), currency);
    }

    private static BigDecimal calcFixedAmount(final BigDecimal fee, final BigDecimal fixedAmount, Currency currency) {
        return BdUtil.setScalePerCurrency(fee.subtract(fixedAmount.min(fee)), currency);
    }
}
