package com.active.services.cart.service.quote.discount.multi;

import com.active.services.Availability;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.domain.DateTime;
import com.active.services.product.AmountType;
import com.active.services.product.discount.multi.DiscountTier;
import com.active.services.product.discount.multi.DiscountTierSortOrder;
import com.active.services.product.discount.multi.MultiDiscount;
import com.active.services.product.discount.multi.MultiDiscountAlgorithm;
import com.active.services.product.discount.multi.MultiDiscountType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static com.active.services.cart.service.quote.discount.multi.loader.MultiDiscountLoaderTestCase.buildCartItem;
import static com.active.services.cart.service.quote.discount.multi.pricer.MultiDiscountBasePricerTestCase.loadCartQuoteContext;

public class CartItemMultiDiscountPricerTestCase {
    private CartItemMultiDiscountPricer itemMultiDiscountPricer;
    private MultiDiscount multiDiscount;
    private DiscountTier discountTier;

    @Before
    public void setUp() {
        // prepare multi discount
        multiDiscount = new MultiDiscount();
        multiDiscount.setId(1L);
        multiDiscount.setDiscountType(MultiDiscountType.MULTI_PERSON);
        multiDiscount.setThreshold(2);
        multiDiscount.setTierSortOrder(DiscountTierSortOrder.ASCENDING_TIER_LEVEL);
        multiDiscount.setAlgorithm(MultiDiscountAlgorithm.LEAST_EXPENSIVE);
        multiDiscount.setAvailability(Availability.ONLINE);
        multiDiscount.setStartDate(new DateTime(LocalDateTime.now().minusDays(1)));
        multiDiscount.setEndDate(new DateTime(LocalDateTime.now().plusDays(1)));

        discountTier = new DiscountTier();
        discountTier.setAmountType(AmountType.FLAT);
        discountTier.setAmount(BigDecimal.TEN);
        discountTier.setTierLevel(1);
        itemMultiDiscountPricer = new CartItemMultiDiscountPricer(discountTier, multiDiscount);
        // prepare CartQuoteContext
        loadCartQuoteContext();
    }

    @Test
    public void quoteWhenAmoutZero() {
        CartItem cartItem = CartDataFactory.cartItem();
        discountTier.setAmount(BigDecimal.ZERO);
        itemMultiDiscountPricer.quote(CartQuoteContext.get(), cartItem);
        Assert.assertEquals(0, cartItem.getFees().get(0).getSubItems().size());

        discountTier.setAmount(BigDecimal.TEN);
        cartItem.setQuantity(0);
        itemMultiDiscountPricer.quote(CartQuoteContext.get(), cartItem);
        Assert.assertEquals(0, cartItem.getFees().get(0).getSubItems().size());
    }

    @Test
    public void quoteSuccess() {
        CartItem item = buildCartItem();
        item.setId(1L);
        CartItemFee fee1 = CartDataFactory.cartItemFee(BigDecimal.valueOf(30));
        item.setFees(Collections.singletonList(fee1));
        itemMultiDiscountPricer.quote(CartQuoteContext.get(), item);
        Assert.assertEquals(1, item.getFees().get(0).getSubItems().size());

        Assert.assertEquals(CartItemFeeType.MULTI_DISCOUNT, item.getFees().get(0).getSubItems().get(0).getType());
        Assert.assertEquals(10, item.getFees().get(0).getSubItems().get(0).getUnitPrice().intValue());
        Assert.assertEquals(1, item.getFees().get(0).getSubItems().get(0).getUnits().intValue());
        int size = CartQuoteContext.get().getAppliedDiscounts().size();
        Assert.assertEquals(1, size);
    }
}
