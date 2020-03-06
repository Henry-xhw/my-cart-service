package com.active.services.cart.service.quote.discount.multi.pricer;

import com.active.services.cart.domain.CartItem;
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
        // two cartItems belong to the same person
        CartItem item1 = buildCartItem();
        item1.setId(1L);
        item1.setNetPrice(new BigDecimal(30));
        item1.setQuantity(1);
        BigDecimal netPrice = item1.getNetPrice();
        CartItem item2 = buildCartItem();
        item2.setId(2L);
        item2.setNetPrice(new BigDecimal(15));
        item2.setPersonIdentifier(item1.getPersonIdentifier());
        item2.setQuantity(1);

        CartItem item3 = buildCartItem();
        item3.setId(3L);
        item3.setNetPrice(new BigDecimal(15));
        item3.setPersonIdentifier(item1.getPersonIdentifier());
        item3.setQuantity(1);

        MultiDiscount multiDiscount = new MultiDiscount();
        multiDiscount.setDiscountType(MultiDiscountType.MULTI_PERSON);
        multiDiscount.setThreshold(2);
        multiDiscount.setAlgorithm(MultiDiscountAlgorithm.LEAST_EXPENSIVE);

        MultiDiscountCartItem mdCartItem = new MultiDiscountCartItem(multiDiscount);
        mdCartItem.addCartItems(Arrays.asList(item1, item2, item3));

        MultiDiscountThresholdSetting multiDiscountThresholdSetting = new MultiDiscountThresholdSetting();
        DiscountTier discountTier = new DiscountTier();
        discountTier.setAmountType(AmountType.FLAT);
        Set<DiscountTier> discountTiers = new HashSet<>();
        discountTiers.add(discountTier);
        multiDiscountThresholdSetting.setTiers(discountTiers);
        MultiPersonDiscountPricer multiPersonDiscountPricer = new MultiPersonDiscountPricer(mdCartItem, multiDiscountThresholdSetting);
        itemBuilder = new PersonBasedEffectiveMdCartItemBuilder(mdCartItem);
        itemBuilder.allProductsComparator(multiPersonDiscountPricer.getAllProductComparator());
         List<CartItem> effectiveItems = itemBuilder.build();
        Assert.assertFalse(effectiveItems.isEmpty());
        Assert.assertEquals(1, effectiveItems.size());
        Assert.assertEquals(item1.getIdentifier(), effectiveItems.get(0).getIdentifier());
    }
}
