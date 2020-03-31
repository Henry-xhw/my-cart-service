package com.active.services.cart.service.quote.discount.multi.builder;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.discount.multi.loader.MultiDiscountCartItem;
import com.active.services.product.discount.multi.MultiDiscount;

import org.junit.Test;

import java.util.Arrays;

import static com.active.services.cart.service.quote.discount.multi.loader.MultiDiscountLoaderTestCase.buildCartItem;
import static org.assertj.core.api.Assertions.assertThat;

public class MultiProductPricerBuilderTestCase {

    @Test
    public void build() {
        MultiDiscount md = new MultiDiscount();
        md.setThreshold(10);
        CartItem item1 = buildCartItem();
        CartItem item2 = buildCartItem();
        MultiDiscountCartItem mdCartItem = new MultiDiscountCartItem(md);
        mdCartItem.addCartItems(Arrays.asList(item1, item2));

        assertThat(new MultiProductPricerBuilder(mdCartItem).build()).isEmpty();
        md.setThreshold(2);
        assertThat(new MultiProductPricerBuilder(mdCartItem).build()).isEmpty();

        CartItem item11 = buildCartItem();
        item11.setPersonIdentifier(item1.getPersonIdentifier());
        CartItem item22 = buildCartItem();
        item22.setPersonIdentifier(item2.getPersonIdentifier());
        mdCartItem.addCartItems(Arrays.asList(item1, item11, item2, item22));
        assertThat(new MultiProductPricerBuilder(mdCartItem).build()).size().isEqualTo(2);
    }
}
