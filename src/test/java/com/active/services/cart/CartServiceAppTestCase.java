package com.active.services.cart;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CartServiceAppTestCase {

    @Test
    public void test() {
        assertThat(new CartServiceApp()).isNotNull();
    }
}
