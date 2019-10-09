package com.active.services.cart.domain.cart;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

@Getter
public class Cart {
    private Long id;
    private UUID uuid;
    private String referenceId;
    private Currency currency;
    private LocalDateTime priceDate;
    private List<CartItem> cartItems;
}
