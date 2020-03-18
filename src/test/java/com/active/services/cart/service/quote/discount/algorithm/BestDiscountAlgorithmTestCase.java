package com.active.services.cart.service.quote.discount.algorithm;

import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.DiscountApplication;
import com.active.services.cart.service.quote.discount.DiscountFactory;
import com.active.services.product.AmountType;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BestDiscountAlgorithmTestCase {

    /**
     * For example, given the following 3 discounts and an amount to discount of $80.00: <br>
     * discount 1 = $20 FLAT <br>
     * discount 2 = 20% PERCENT <br>
     * discount 3 = $70 FIXED_AMOUNT <br>
     * Discount #1 provides the biggest savings to the consumer ($20, versus $16 and $10): <br>
     * discount 1 = $20 <br>
     * discount 2 = 20% x $80 = $16 <br>
     * discount 3 = 80-70 = $10 <br>
     */
    @Test
    public void apply() {

        Cart cart = CartDataFactory.cart();
        CartItem cartItem = cart.getFlattenCartItems().get(0);
        CartQuoteContext cartQuoteContext = new CartQuoteContext(cart);

        DiscountApplication discountApplication1 = DiscountFactory.getCouponCodeDiscountApplication(
                AmountType.FLAT, new BigDecimal("20.00"), "code", null, null, null, cartQuoteContext);

        DiscountApplication discountApplication2 = DiscountFactory.getCouponCodeDiscountApplication(
                AmountType.PERCENT, new BigDecimal("20.00"), "code", null, null, null, cartQuoteContext);

        DiscountApplication discountApplication3 = DiscountFactory.getCouponCodeDiscountApplication(
                AmountType.FIXED_AMOUNT, new BigDecimal("70.00"), "code", null, null, null, cartQuoteContext);

        BestDiscountAlgorithm discountAlgorithm = new BestDiscountAlgorithm(cartItem, Currency.getInstance("USD"));
        List<DiscountApplication>  discountApplications = discountAlgorithm.apply(Arrays.asList(discountApplication1,
                discountApplication2,
                discountApplication3));
        assertEquals(1, discountApplications.size());
        assertTrue(discountApplications.contains(discountApplication1));
    }

}
