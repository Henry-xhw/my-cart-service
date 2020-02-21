package com.active.services.cart.service.quote.discount.condition;

import com.active.services.cart.client.soap.ProductServiceSoap;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.domain.DateTime;
import com.active.services.product.AmountType;
import com.active.services.product.Discount;
import com.active.services.product.DiscountAlgorithm;
import com.active.services.product.DiscountType;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DiscountSpecsTestCase {

    @MockBean
    private ProductServiceSoap productRepo;
    @Autowired
    private DiscountSpecs discountSpecs;

    @Test
    public void couponDiscountWithNotExpiredSpec() {
        Cart cart = CartDataFactory.cart();
        CartQuoteContext cartQuoteContext = new CartQuoteContext(cart);
        CartItem cartItem = cart.getFlattenCartItems().get(0);
        Discount discount = getDiscount(AmountType.FLAT, new BigDecimal("2.00"), "code");

        DiscountSpecification discountSpecification = discountSpecs.couponDiscount(cartQuoteContext, cartItem,
                discount);
        assertTrue(discountSpecification.satisfy());
        discount.setStartDate(new DateTime(LocalDateTime.now().plusHours(1)));
        discountSpecification = discountSpecs.couponDiscount(cartQuoteContext, cartItem,
                discount);
        assertFalse(discountSpecification.satisfy());
    }

    @Test
    public void couponDiscountWithNotUniqueUsedSpec() {
        Cart cart = CartDataFactory.cart();
        CartQuoteContext cartQuoteContext = new CartQuoteContext(cart);
        CartItem cartItem = cart.getFlattenCartItems().get(0);
        Discount discount = getDiscount(AmountType.FLAT, new BigDecimal("2.00"), "code");

        DiscountSpecification discountSpecification = discountSpecs.couponDiscount(cartQuoteContext, cartItem,
                discount);
        assertTrue(discountSpecification.satisfy());

        discount.setDiscountAlgorithm(DiscountAlgorithm.MOST_EXPENSIVE);
        cartQuoteContext.addAppliedDiscount(new com.active.services.cart.service.quote.discount.Discount(
                "name", "disc", discount.getAmount(), discount.getAmountType(),
                discount.getId(), DiscountType.COUPON,
                discount.getCouponCode(), discount.getDiscountAlgorithm()
        ));

        discountSpecification = discountSpecs.couponDiscount(cartQuoteContext, cartItem,
                discount);

        assertFalse(discountSpecification.satisfy());
    }

    private Discount getDiscount(AmountType type, BigDecimal amount, String code) {
        Discount discount = new Discount();
        discount.setId(RandomUtils.nextLong());
        discount.setCouponCode(code);
        discount.setAmountType(type);
        discount.setAmount(amount);
        DateTime start = new DateTime(LocalDateTime.now().minusDays(1));
        DateTime end = new DateTime(LocalDateTime.now().plusDays(1));
        discount.setStartDate(start);
        discount.setEndDate(end);
        return discount;
    }
}
