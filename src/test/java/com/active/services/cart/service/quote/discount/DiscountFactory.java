package com.active.services.cart.service.quote.discount;

import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.domain.DateTime;
import com.active.services.product.AmountType;
import com.active.services.product.Discount;
import com.active.services.product.DiscountAlgorithm;

import org.apache.commons.lang.math.RandomUtils;

import java.math.BigDecimal;

public class DiscountFactory {
    public static Discount getDiscount(AmountType type, BigDecimal amount, String code, DiscountAlgorithm algorithm,
                                       DateTime start, DateTime end) {
        Discount discount = new Discount();
        discount.setId(RandomUtils.nextLong());
        discount.setName("name");
        discount.setDescription("desc");
        discount.setCouponCode(code);
        discount.setAmountType(type);
        discount.setAmount(amount);
        discount.setDiscountAlgorithm(algorithm);
        discount.setStartDate(start);
        discount.setEndDate(end);
        discount.setApplyToRecurringBilling(true);
        return discount;
    }

    public static com.active.services.cart.domain.Discount getCouponCodeDiscountApplication(AmountType type, BigDecimal amount, String code,
                                                                                            DiscountAlgorithm algorithm, DateTime start, DateTime end,
                                                                                            CartQuoteContext context) {
        return DiscountMapper.MAPPER.toDiscount(getDiscount(type, amount, code, algorithm, start, end), context);
    }
}
