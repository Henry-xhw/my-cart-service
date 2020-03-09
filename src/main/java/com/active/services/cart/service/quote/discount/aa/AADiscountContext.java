package com.active.services.cart.service.quote.discount.aa;

import com.active.services.oms.BdUtil;
import com.active.services.product.discount.aa.AaDiscount;

import lombok.Data;

import java.math.BigDecimal;

@Data
class AADiscountContext {

    private final AaDiscount aaDiscount;

    private BigDecimal remainingDiscAmt;

    AADiscountContext(AaDiscount aaDiscount) {
        this.aaDiscount = aaDiscount;
        remainingDiscAmt = aaDiscount.getMaximumAmount();
    }

    public boolean hasRemainingAmt() {
        return BdUtil.isGreaterThanZero(remainingDiscAmt);
    }

    public void subtractDiscountAmt(BigDecimal discountAmt) {
        remainingDiscAmt = remainingDiscAmt.subtract(discountAmt);
    }
}
