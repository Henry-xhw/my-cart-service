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
import java.util.UUID;

import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class DiscountConvertorTestCase {

    @Test
    public void couponDiscountWithNotExpiredSpec() {

        Cart cart = CartDataFactory.cart();
        CartQuoteContext cartQuoteContext = new CartQuoteContext(cart);
        Discount discount = DiscountFactory.getDiscount(AmountType.FLAT, new BigDecimal("2.00"), "code",
                DiscountAlgorithm.MOST_EXPENSIVE,
                new DateTime(LocalDateTime.now().minusDays(1)),
                new DateTime(LocalDateTime.now().plusDays(1)));

        DiscountApplication application = DiscountConvertor.convert(discount, cartQuoteContext);

        assertThat(Collections.singleton(application)).extracting("name", "description", "algorithm", "amount", "amountType",
                "couponCode", "discountId", "applyToRecurringBilling", "discountType")
                .contains(tuple(discount.getName(), discount.getDescription(), discount.getDiscountAlgorithm(),
                        discount.getAmount(), discount.getAmountType(), discount.getCouponCode(), discount.getId(),
                        discount.getApplyToRecurringBilling(), DiscountType.COUPON));

        assertNotNull(application.getCondition());
        assertTrue(application.satisfy());

        discount.setStartDate(new DateTime(LocalDateTime.now().plusHours(1)));

        assertNotNull(application.getCondition());
        application = DiscountConvertor.convert(discount, cartQuoteContext);
        assertFalse(application.satisfy());

    }

    @Test
    public void couponDiscountWithNotUniqueUsedSpec() {
        Cart cart = CartDataFactory.cart();
        CartQuoteContext cartQuoteContext = new CartQuoteContext(cart);
        Discount discount = DiscountFactory.getDiscount(AmountType.PERCENT, new BigDecimal("3.22"), "code",
                DiscountAlgorithm.MOST_EXPENSIVE,
                new DateTime(LocalDateTime.now().minusDays(1)),
                new DateTime(LocalDateTime.now().plusDays(1)));
        discount.setDiscountAlgorithm(DiscountAlgorithm.MOST_EXPENSIVE);
        cartQuoteContext.addAppliedDiscount(new DiscountApplication(
                "name", "disc", discount.getAmount(), discount.getAmountType(),
                discount.getId(), DiscountType.COUPON,
                discount.getCouponCode(), discount.getDiscountAlgorithm(), null, false,
                UUID.randomUUID(),
                cart.getId()));

        DiscountApplication application = DiscountConvertor.convert(discount, cartQuoteContext);

        assertNotNull(application.getCondition());
        assertFalse(application.satisfy());
    }

}

