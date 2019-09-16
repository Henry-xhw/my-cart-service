package com.active.services.cart.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddItemToCartReq {
    private String cartIdentifier;
    private CartItemDto cartItemDto;
}
