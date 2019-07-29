package com.active.services.cart;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class CartServiceAppTestCase {

    @Test
    public void test() {
        assertThat(new CartServiceApp()).isNotNull();
    }
}
