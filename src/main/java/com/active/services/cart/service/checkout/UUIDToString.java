package com.active.services.cart.service.checkout;

public final class UUIDToString {

    private UUIDToString() {
        // prevent construct
    }

    public static String map(java.util.UUID value) {
        if (value == null) {
            return null;
        }

        return value.toString();
    }
}
