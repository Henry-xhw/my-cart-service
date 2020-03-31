package com.active.services.cart.service.quote.discount;

import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.domain.DateTime;
import com.active.services.order.discount.membership.MembershipDiscountsHistory;
import com.active.services.product.AmountType;
import com.active.services.product.Discount;
import com.active.services.product.DiscountAlgorithm;
import com.active.services.product.DiscountType;

import org.apache.commons.lang.math.RandomUtils;
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

    @Test
    public void membershipDiscount() {

        Cart cart = CartDataFactory.cart();
        CartQuoteContext cartQuoteContext = new CartQuoteContext(cart);
        MembershipDiscountsHistory membershipDiscountsHistory = new MembershipDiscountsHistory();
        membershipDiscountsHistory.setName("membership discount name");
        membershipDiscountsHistory.setDescription("membership discount desc");
        membershipDiscountsHistory.setMembershipId(RandomUtils.nextLong());
        membershipDiscountsHistory.setAmount(new BigDecimal("20.23"));
        membershipDiscountsHistory.setAmountType(AmountType.FIXED_AMOUNT);
        membershipDiscountsHistory.setStartDate(new DateTime(LocalDateTime.now().minusDays(1)));
        membershipDiscountsHistory.setId(RandomUtils.nextLong());
        com.active.services.cart.domain.Discount application = DiscountMapper.MAPPER.toDiscount(membershipDiscountsHistory, cartQuoteContext);

        assertThat(Collections.singleton(application)).extracting("name", "description", "amount", "amountType",
                "discountId", "discountType", "membershipId", "startDate", "endDate")
                .contains(tuple(membershipDiscountsHistory.getName(), membershipDiscountsHistory.getDescription(),
                        membershipDiscountsHistory.getAmount(), membershipDiscountsHistory.getAmountType(),
                        membershipDiscountsHistory.getId(), DiscountType.MEMBERSHIP,
                        membershipDiscountsHistory.getMembershipId(),
                        membershipDiscountsHistory.getStartDate().toDate().toInstant(),
                        null));
    }
}

