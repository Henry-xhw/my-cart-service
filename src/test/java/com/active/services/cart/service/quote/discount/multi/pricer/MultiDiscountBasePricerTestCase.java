package com.active.services.cart.service.quote.discount.multi.pricer;

import com.active.services.Availability;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.multi.loader.MultiDiscountCartItem;
import com.active.services.domain.DateTime;
import com.active.services.product.AmountType;
import com.active.services.product.discount.multi.DiscountTier;
import com.active.services.product.discount.multi.DiscountTierSortOrder;
import com.active.services.product.discount.multi.MultiDiscount;
import com.active.services.product.discount.multi.MultiDiscountAlgorithm;
import com.active.services.product.discount.multi.MultiDiscountThresholdSetting;
import com.active.services.product.discount.multi.MultiDiscountType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.active.services.cart.service.quote.discount.multi.loader.MultiDiscountLoaderTestCase.buildCartItem;

public class MultiDiscountBasePricerTestCase {
    private MultiDiscountBasePricer multiDiscountBasePricer;
    private List<CartItem> toPriceCartItems = new ArrayList<>();
    private MultiDiscountThresholdSetting multiDiscountThresholdSetting;
    private MultiDiscount multiDiscount;
    private MultiDiscountCartItem mdCartItem;

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

        mdCartItem = new MultiDiscountCartItem(multiDiscount);
        mdCartItem.addCartItems(toPriceCartItems);

        // prepare DiscountThresholdSetting
        multiDiscountThresholdSetting = new MultiDiscountThresholdSetting();
        DiscountTier discountTier = new DiscountTier();
        discountTier.setAmountType(AmountType.FLAT);
        discountTier.setAmount(BigDecimal.TEN);
        discountTier.setTierLevel(1);

        Set<DiscountTier> discountTiers = new HashSet<>();
        discountTiers.add(discountTier);
        multiDiscountThresholdSetting.setTiers(discountTiers);
        multiDiscountBasePricer = new MultiPersonDiscountPricer(mdCartItem, multiDiscountThresholdSetting);

        // prepare CartQuoteContext
        loadCartQuoteContext();
    }

    @Test
    public void priceByMultiPersonPricerWhenNoPriceOnItem() {
        // case 1: All items does not have net price
        loadCartItems(false, false);
        multiDiscountBasePricer.getMdCartItem().getCartItems().clear();
        multiDiscountBasePricer.getMdCartItem().addCartItems(toPriceCartItems);
        multiDiscountBasePricer.price();
        boolean allMatch = multiDiscountBasePricer.getMdCartItem()
                .getCartItems()
                .stream()
                .allMatch(cartItem -> cartItem.getPriceCartItemFee().isPresent());
        Assert.assertFalse(allMatch);
    }

    @Test
    public void priceByMultiPersonPricerWhenTierTypeFlatSamePersonLeastExpen() {
        // case 2: add item net price, multiDiscountThresholdSetting type:Flat, all items belong to the same preson
        loadCartItems(true, true);
        loadCartQuoteContext();
        multiDiscountBasePricer.getMdCartItem().getCartItems().clear();
        multiDiscountBasePricer.getMdCartItem().addCartItems(toPriceCartItems);
        multiDiscountBasePricer.price();
        CartItemFee checkFee = toPriceCartItems
                .stream()
                .filter(cartItem -> cartItem.getId() == 2)
                .collect(Collectors.toList()).get(0).getFees().get(0);
        Assert.assertTrue(checkFee.getSubItems().size() != 0);
        Assert.assertEquals(CartItemFeeType.MULTI_DISCOUNT, checkFee.getSubItems().get(0).getType());
        Assert.assertEquals(10, checkFee.getSubItems().get(0).getUnitPrice().intValue());
        Assert.assertEquals(1, checkFee.getSubItems().get(0).getUnits().intValue());
    }

    @Test
    public void priceByMultiPersonPricerWhenTierTypePercentSamePersonMostExpen() {
        // case 3: multiDiscountThresholdSetting type:PERCENT, most expensive, all items belong to the same preson
        loadCartItems(true, true);
        loadCartQuoteContext();
        multiDiscountBasePricer.getMdCartItem().getCartItems().clear();
        multiDiscountBasePricer.getMdCartItem().addCartItems(toPriceCartItems);
        multiDiscount.setAlgorithm(MultiDiscountAlgorithm.MOST_EXPENSIVE);
        multiDiscountThresholdSetting.getTiers().forEach(tire -> tire.setAmountType(AmountType.PERCENT));
        multiDiscountBasePricer.price();
        CartItemFee checkFee = toPriceCartItems
                .stream()
                .filter(cartItem -> cartItem.getId() == 1)
                .collect(Collectors.toList()).get(0).getFees().get(0);
        Assert.assertTrue(checkFee.getSubItems().size() != 0);
        Assert.assertEquals(CartItemFeeType.MULTI_DISCOUNT, checkFee.getSubItems().get(0).getType());
        Assert.assertEquals(3, checkFee.getSubItems().get(0).getUnitPrice().intValue());
        Assert.assertEquals(1, checkFee.getSubItems().get(0).getUnits().intValue());
    }

    @Test
    public void priceByMultiPersonPricerWhenTierTypeFixedSamePersonMostExpen() {
        // case 4: multiDiscountThresholdSetting type:FIXED_AMOUNT, most expensive, all items belong to the same preson
        loadCartItems(true, true);
        loadCartQuoteContext();
        multiDiscountBasePricer.getMdCartItem().getCartItems().clear();
        multiDiscountBasePricer.getMdCartItem().addCartItems(toPriceCartItems);
        multiDiscount.setAlgorithm(MultiDiscountAlgorithm.MOST_EXPENSIVE);
        multiDiscountThresholdSetting.getTiers().forEach(tire -> tire.setAmountType(AmountType.FIXED_AMOUNT));
        multiDiscountBasePricer.price();
        CartItemFee checkFee = toPriceCartItems
                .stream()
                .filter(cartItem -> cartItem.getId() == 1)
                .collect(Collectors.toList()).get(0).getFees().get(0);
        Assert.assertTrue(checkFee.getSubItems().size() != 0);
        Assert.assertEquals(CartItemFeeType.MULTI_DISCOUNT, checkFee.getSubItems().get(0).getType());
        Assert.assertEquals(20, checkFee.getSubItems().get(0).getUnitPrice().intValue());
        Assert.assertEquals(1, checkFee.getSubItems().get(0).getUnits().intValue());
    }

    @Test
    public void priceByMultiPersonPricerWhenTierTypeFlatMultiPersonMostExpen() {
        // case 5: multiDiscountThresholdSetting type:FLAT, most expensive, all items belong to multi preson
        loadCartQuoteContext();
        loadCartItems(true, false);
        multiDiscountBasePricer.getMdCartItem().getCartItems().clear();
        multiDiscountBasePricer.getMdCartItem().addCartItems(toPriceCartItems);
        multiDiscountThresholdSetting.getTiers().forEach(tire -> tire.setAmountType(AmountType.FLAT));
        multiDiscount.setAlgorithm(MultiDiscountAlgorithm.MOST_EXPENSIVE);
        multiDiscountBasePricer.price();
        Assert.assertEquals(4, toPriceCartItems
                .stream().filter(cartItem -> cartItem.getFees().get(0).getSubItems().size() != 0).count());

        // check one of the cart item is ok
        CartItemFee checkFee = toPriceCartItems
                .stream()
                .filter(cartItem -> cartItem.getId() == 1)
                .collect(Collectors.toList()).get(0).getFees().get(0);
        Assert.assertEquals(CartItemFeeType.MULTI_DISCOUNT, checkFee.getSubItems().get(0).getType());
        Assert.assertEquals(10, checkFee.getSubItems().get(0).getUnitPrice().intValue());
        Assert.assertEquals(1, checkFee.getSubItems().get(0).getUnits().intValue());
    }


    @Test
    public void priceByMultiPersonPricerWhenTierTypeFlatMultiPersonMostExpenNotReachTierLevel() {
        // case 6: add item net price,
        // multiDiscountThresholdSetting type:Flat,
        // all items belong to the same preson， tire level set 5
        loadCartItems(true, true);
        loadCartQuoteContext();
        multiDiscountThresholdSetting.getTiers().forEach(tire -> tire.setTierLevel(5));
        multiDiscountBasePricer.getMdCartItem().getCartItems().clear();
        multiDiscountBasePricer.getMdCartItem().addCartItems(toPriceCartItems);
        multiDiscount.setAlgorithm(MultiDiscountAlgorithm.MOST_EXPENSIVE);
        multiDiscountBasePricer.price();
        long count = toPriceCartItems
                .stream()
                .filter(cartItem -> cartItem.getFees().get(0).getSubItems().size() != 0).count();
        Assert.assertEquals(0, count);
    }

    @Test
    public void priceByMultiProductPricerWhenTierTypeFlatSamePersonLeastExpen() {
        // case 7: add item net price, multiDiscountThresholdSetting type:Flat, all items belong to the same preson
        loadCartItems(true, true);
        loadCartQuoteContext();
        multiDiscountBasePricer = new MultiProductDiscountPricer(mdCartItem, multiDiscountThresholdSetting);
        multiDiscountBasePricer.getMdCartItem().getCartItems().clear();
        multiDiscountBasePricer.getMdCartItem().addCartItems(toPriceCartItems);
        multiDiscountBasePricer.price();
        CartItemFee checkFee = toPriceCartItems
                .stream()
                .filter(cartItem -> cartItem.getId() == 2)
                .collect(Collectors.toList()).get(0).getFees().get(0);
        Assert.assertTrue(checkFee.getSubItems().size() != 0);
        Assert.assertEquals(CartItemFeeType.MULTI_DISCOUNT, checkFee.getSubItems().get(0).getType());
        Assert.assertEquals(10, checkFee.getSubItems().get(0).getUnitPrice().intValue());
        Assert.assertEquals(1, checkFee.getSubItems().get(0).getUnits().intValue());
    }

    @Test
    public void priceByMultiProductPricerWhenTierTypeFlatSamePersonMostExpenNotReachTierLevel() {
        // case 8: add item net price,
        // multiDiscountThresholdSetting type:Flat,
        // all items belong to the same preson， tire level set 5
        loadCartItems(true, true);
        loadCartQuoteContext();
        multiDiscountThresholdSetting.getTiers().forEach(tire -> tire.setTierLevel(5));
        multiDiscountBasePricer = new MultiProductDiscountPricer(mdCartItem, multiDiscountThresholdSetting);
        multiDiscountBasePricer.getMdCartItem().getCartItems().clear();
        multiDiscountBasePricer.getMdCartItem().addCartItems(toPriceCartItems);
        multiDiscount.setAlgorithm(MultiDiscountAlgorithm.MOST_EXPENSIVE);
        multiDiscountBasePricer.price();
        long count = toPriceCartItems
                .stream()
                .filter(cartItem -> cartItem.getFees().get(0).getSubItems().size() != 0).count();

        Assert.assertEquals(0, count);
    }

    @Test
    public void priceByMultiProductPricerWhenTierTypeFlatMultiPersonMostExpen() {
        // case 9: add item net price,
        // multiDiscountThresholdSetting type:Flat,
        // all items belong to the same preson， tire level set 3
        loadCartItems(true, false);
        loadCartQuoteContext();
        multiDiscountThresholdSetting.getTiers().forEach(tire -> tire.setTierLevel(3));
        multiDiscountBasePricer = new MultiProductDiscountPricer(mdCartItem, multiDiscountThresholdSetting);
        multiDiscountBasePricer.getMdCartItem().getCartItems().clear();
        multiDiscountBasePricer.getMdCartItem().addCartItems(toPriceCartItems);
        multiDiscount.setAlgorithm(MultiDiscountAlgorithm.MOST_EXPENSIVE);
        multiDiscountBasePricer.price();
        long discountNum = toPriceCartItems
                .stream()
                .filter(cartItem -> cartItem.getFees().get(0).getSubItems().size() != 0).count();
        Assert.assertEquals(2, discountNum);
        List<CartItem> collect = toPriceCartItems
                .stream()
                .filter(cartItem -> cartItem.getFees().get(0).getSubItems().size() != 0)
                .collect(Collectors.toList());

        Assert.assertEquals(CartItemFeeType.MULTI_DISCOUNT,
                collect.get(0).getFees().get(0).getSubItems().get(0).getType());
        Assert.assertEquals(10, collect.get(0).getFees().get(0).getSubItems().get(0).getUnitPrice().intValue());
        Assert.assertEquals(1, collect.get(0).getFees().get(0).getSubItems().get(0).getUnits().intValue());

        Assert.assertEquals(CartItemFeeType.MULTI_DISCOUNT,
                collect.get(1).getFees().get(0).getSubItems().get(0).getType());
        Assert.assertEquals(10, collect.get(1).getFees().get(0).getSubItems().get(0).getUnitPrice().intValue());
        Assert.assertEquals(1, collect.get(1).getFees().get(0).getSubItems().get(0).getUnits().intValue());

    }


    private void loadCartItems(boolean fillFee, boolean isSamePerson) {
        toPriceCartItems.clear();
        CartItem item1 = buildCartItem();
        item1.setId(1L);
        CartItem item2 = buildCartItem();
        item2.setId(2L);
        CartItem item3 = buildCartItem();
        item3.setId(3L);
        CartItem item4 = buildCartItem();
        item4.setId(4L);
        if (fillFee) {
            CartItemFee fee1 = CartDataFactory.cartItemFee(BigDecimal.valueOf(30));
            item1.setFees(Collections.singletonList(fee1));
            CartItemFee fee2 = CartDataFactory.cartItemFee(BigDecimal.valueOf(15));
            item2.setFees(Collections.singletonList(fee2));
            CartItemFee fee3 = CartDataFactory.cartItemFee(BigDecimal.valueOf(15));
            item3.setFees(Collections.singletonList(fee3));
            CartItemFee fee4 = CartDataFactory.cartItemFee(BigDecimal.valueOf(30));
            item4.setFees(Collections.singletonList(fee4));
        }

        if (isSamePerson) {
            item2.setPersonIdentifier(item1.getPersonIdentifier());
            item3.setPersonIdentifier(item1.getPersonIdentifier());
            item4.setPersonIdentifier(item1.getPersonIdentifier());
        }

        toPriceCartItems.addAll(Arrays.asList(item1, item2, item3, item4));
    }

    private void loadCartQuoteContext() {
        CartQuoteContext.destroy();
        Cart cart = CartDataFactory.cart();
        CartQuoteContext cartQuoteContext = new CartQuoteContext(cart);
        CartQuoteContext.set(cartQuoteContext);
    }
}
