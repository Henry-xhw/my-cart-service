package com.active.services.cart.service.quote.discount;

import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.domain.Discount;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.domain.DateTime;
import com.active.services.product.AmountType;
import com.active.services.product.DiscountAlgorithm;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class DiscountFeeLoaderTestCase {

    @Test
    public void apply() {
        Cart cart = CartDataFactory.cart();
        CartQuoteContext cartQuoteContext = new CartQuoteContext(cart);
        Discount code = DiscountFactory.getCouponCodeDiscountApplication(AmountType.FLAT,
                new BigDecimal("2.00"), "code",
                DiscountAlgorithm.MOST_EXPENSIVE,
                new DateTime(LocalDateTime.now().minusDays(1)), new DateTime(LocalDateTime.now().plusDays(1)),
                cartQuoteContext);

        CartItem item = CartDataFactory.cartItem();
        DiscountFeeLoader discountFeeLoader = new DiscountFeeLoader(cartQuoteContext, item, code);
        discountFeeLoader.load();
        assertEquals(cartQuoteContext.getAppliedDiscounts().size(), 1);
        CartItemFee cartItemFee = item.getPriceCartItemFee().get();
        CartItemFee cartItemFee1 = cartItemFee.getSubItems().stream()
                .filter(f -> CartItemFeeType.isPriceDiscount(f.getType())).findAny().get();
        assertEquals(cartItemFee1.getDiscountIdentifier(), code.getIdentifier());
    }
}
