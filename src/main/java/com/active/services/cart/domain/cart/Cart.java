package com.active.services.cart.domain.cart;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Cart {
    private Long id;
    private UUID uuid;
    private String referenceId;
    private String currency;
    private LocalDateTime priceDate;
    private List<CartItem> cartItems;
}
