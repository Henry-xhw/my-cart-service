package com.active.services.cart.service.quote.discount.multi.pricer;

import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.service.quote.discount.multi.loader.MultiDiscountCartItem;
import com.active.services.product.AmountType;
import com.active.services.product.discount.multi.DiscountTier;
import com.active.services.product.discount.multi.MultiDiscount;
import com.active.services.product.discount.multi.MultiDiscountAlgorithm;
import com.active.services.product.discount.multi.MultiDiscountThresholdSetting;
import com.active.services.product.discount.multi.MultiDiscountType;

import org.checkerframework.checker.units.qual.C;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.active.services.cart.service.quote.discount.multi.loader.MultiDiscountLoaderTestCase.buildCartItem;

public class PersonBasedEffectiveMdCartItemBuilderTestCase {

    private PersonBasedEffectiveMdCartItemBuilder itemBuilder;

    @Test
    public void buildTest(){
        // four cartItems belong to the same person
        CartItem item1 = buildCartItem();
        item1.setId(1L);
        CartItemFee fee1 = CartDataFactory.cartItemFee(BigDecimal.valueOf(30));
        item1.setQuantity(1);
        item1.setFees(Arrays.asList(fee1));

        CartItem item2 = buildCartItem();
        item2.setId(2L);
        CartItemFee fee2 = CartDataFactory.cartItemFee(BigDecimal.valueOf(15));
        item2.setFees(Arrays.asList(fee2));
        item2.setPersonIdentifier(item1.getPersonIdentifier());
        item2.setQuantity(1);

        CartItem item3 = buildCartItem();
        item3.setId(3L);
        CartItemFee fee3 = CartDataFactory.cartItemFee(BigDecimal.valueOf(15));
        item3.setFees(Arrays.asList(fee3));
        item3.setPersonIdentifier(item1.getPersonIdentifier());
        item3.setQuantity(1);

        CartItem item4 = buildCartItem();
        item4.setId(4L);
        CartItemFee fee4 = CartDataFactory.cartItemFee(BigDecimal.valueOf(30));
        item4.setFees(Arrays.asList(fee4));
        item4.setPersonIdentifier(item1.getPersonIdentifier());
        item4.setQuantity(1);

        // case 1:LEAST_EXPENSIVE
        MultiDiscount multiDiscount = new MultiDiscount();
        multiDiscount.setDiscountType(MultiDiscountType.MULTI_PERSON);
        multiDiscount.setThreshold(2);
        multiDiscount.setAlgorithm(MultiDiscountAlgorithm.LEAST_EXPENSIVE);

        MultiDiscountCartItem mdCartItem = new MultiDiscountCartItem(multiDiscount);
        mdCartItem.addCartItems(Arrays.asList(item4, item2, item3, item1));

        MultiDiscountThresholdSetting multiDiscountThresholdSetting = new MultiDiscountThresholdSetting();
        DiscountTier discountTier = new DiscountTier();
        discountTier.setAmountType(AmountType.FLAT);
        Set<DiscountTier> discountTiers = new HashSet<>();
        discountTiers.add(discountTier);
        multiDiscountThresholdSetting.setTiers(discountTiers);
        MultiPersonDiscountPricer multiPersonDiscountPricer = new MultiPersonDiscountPricer(mdCartItem, multiDiscountThresholdSetting);
        itemBuilder = new PersonBasedEffectiveMdCartItemBuilder(mdCartItem);
        itemBuilder.allProductsComparator(multiPersonDiscountPricer.getAllProductComparator());

        List<CartItem> leastExpItems = itemBuilder.build();
        Assert.assertFalse(leastExpItems.isEmpty());
        Assert.assertEquals(1, leastExpItems.size());
        Assert.assertEquals(2L, leastExpItems.get(0).getId().longValue());

        // case most expensive
        multiDiscount.setAlgorithm(MultiDiscountAlgorithm.MOST_EXPENSIVE);
        List<CartItem> mostExpItems = itemBuilder.build();
        Assert.assertFalse(mostExpItems.isEmpty());
        Assert.assertEquals(1, mostExpItems.size());
        Assert.assertEquals(1L, mostExpItems.get(0).getId().longValue());

        // case ALL_PRODUCTS
        multiDiscount.setAlgorithm(MultiDiscountAlgorithm.ALL_PRODUCTS);
        List<CartItem> allPrdItems = itemBuilder.build();
        Assert.assertFalse(allPrdItems.isEmpty());
        Assert.assertEquals(4, allPrdItems.size());

    }
}
