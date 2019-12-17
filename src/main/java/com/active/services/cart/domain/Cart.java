package com.active.services.cart.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.active.services.cart.model.CurrencyCode;

import lombok.Data;

@Data
public class Cart extends BaseDomainObject {

    private UUID ownerId;

    private UUID keyerId;

    private CurrencyCode currencyCode;

    private List<CartItem> items = new ArrayList<>();

    private List<CartFee> fees = new ArrayList<>();
}