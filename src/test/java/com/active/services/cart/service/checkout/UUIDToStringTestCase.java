package com.active.services.cart.service.checkout;

import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class UUIDToStringTestCase {

    @Test
    public void testUUID2String() {
        UUID uuid = null;
        assertThat(UUIDToString.map(uuid)).isNull();

        uuid = UUID.randomUUID();
        assertThat(UUIDToString.map(uuid)).isEqualTo(uuid.toString());
    }
}
