package com.active.services.cart.service.quote.discount;

import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.domain.DateTime;
import com.active.services.product.AmountType;
import com.active.services.product.Discount;
import com.active.services.product.DiscountAlgorithm;
import com.active.services.product.DiscountType;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class DiscountMapperTestCase {

    @Test
    public void couponDiscount() {

        Cart cart = CartDataFactory.cart();
        CartQuoteContext cartQuoteContext = new CartQuoteContext(cart);
        Discount discount = DiscountFactory.getDiscount(AmountType.FLAT, new BigDecimal("2.00"), "code",
                DiscountAlgorithm.MOST_EXPENSIVE,
                new DateTime(LocalDateTime.now().minusDays(1)),
                new DateTime(LocalDateTime.now().plusDays(1)));

        com.active.services.cart.domain.Discount application = DiscountMapper.MAPPER.toDiscount(discount, cartQuoteContext);

        assertThat(Collections.singleton(application)).extracting("name", "description", "algorithm", "amount", "amountType",
                "couponCode", "discountId", "applyToRecurringBilling", "discountType")
                .contains(tuple(discount.getName(), discount.getDescription(), discount.getDiscountAlgorithm(),
                        discount.getAmount(), discount.getAmountType(), discount.getCouponCode(), discount.getId(),
                        discount.getApplyToRecurringBilling(), DiscountType.COUPON));
    }
}

