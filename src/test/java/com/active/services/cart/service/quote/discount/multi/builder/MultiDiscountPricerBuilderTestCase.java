package com.active.services.cart.service.quote.discount.multi.builder;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.discount.multi.loader.MultiDiscountCartItem;
import com.active.services.product.discount.multi.MultiDiscount;
import com.active.services.product.discount.multi.MultiDiscountType;

import org.junit.Test;

import java.util.Arrays;

import static com.active.services.cart.service.quote.discount.multi.loader.MultiDiscountLoaderTestCase.buildCartItem;
import static org.assertj.core.api.Assertions.assertThat;

public class MultiDiscountPricerBuilderTestCase {
    @Test
    public void build() {
        CartItem item1 = buildCartItem();
        CartItem item2 = buildCartItem();

        MultiDiscount md1 = new MultiDiscount();
        md1.setDiscountType(MultiDiscountType.MULTI_PERSON);
        md1.setThreshold(2);
        MultiDiscountCartItem mdCartItem1 = new MultiDiscountCartItem(md1);
        mdCartItem1.addCartItems(Arrays.asList(item1, item2));

        MultiDiscount md2 = new MultiDiscount();
        md2.setDiscountType(MultiDiscountType.MULTI_PRODUCT);
        md2.setThreshold(2);
        MultiDiscountCartItem mdCartItem2 = new MultiDiscountCartItem(md2);
        CartItem item11 = buildCartItem();
        item11.setPersonIdentifier(item1.getPersonIdentifier());
        CartItem item22 = buildCartItem();
        item22.setPersonIdentifier(item2.getPersonIdentifier());
        mdCartItem2.addCartItems(Arrays.asList(item1, item11, item2, item22));

        assertThat(new MultiDiscountPricerBuilder().multiDiscountCartItems(Arrays.asList(mdCartItem1, mdCartItem2))
                .build()).size().isEqualTo(3);
    }
}
