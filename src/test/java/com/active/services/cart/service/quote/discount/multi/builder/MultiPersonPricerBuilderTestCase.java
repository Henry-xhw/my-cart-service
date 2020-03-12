package com.active.services.cart.service.quote.discount.multi.builder;

import com.active.services.cart.service.quote.discount.multi.loader.MultiDiscountCartItem;
import com.active.services.product.discount.multi.MultiDiscount;

import org.junit.Test;

import java.util.Arrays;

import static com.active.services.cart.service.quote.discount.multi.loader.MultiDiscountLoaderTestCase.buildCartItem;
import static org.assertj.core.api.Assertions.assertThat;

public class MultiPersonPricerBuilderTestCase {

    @Test
    public void build() {
        MultiDiscount md = new MultiDiscount();
        md.setThreshold(10);
        MultiDiscountCartItem mdCartItem = new MultiDiscountCartItem(md);
        mdCartItem.addCartItems(Arrays.asList(buildCartItem(), buildCartItem()));

        assertThat(new MultiPersonPricerBuilder(mdCartItem).build()).isEmpty();
        md.setThreshold(1);
        assertThat(new MultiPersonPricerBuilder(mdCartItem).build()).size().isEqualTo(1);
    }
}
