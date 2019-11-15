package com.active.services.cart.model.v1;

import com.active.services.cart.model.CurrencyCode;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class CartDto extends BaseDto {

    private UUID ownerId;

    private UUID keyerId;

    private CurrencyCode currency;

    private List<CartItemDto> items = new ArrayList<>();
}
