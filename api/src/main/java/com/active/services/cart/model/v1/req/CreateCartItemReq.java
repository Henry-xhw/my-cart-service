package com.active.services.cart.model.v1.req;

import com.active.services.cart.model.v1.CartItemDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class CreateCartItemReq {

    private UUID cartId;

    private List<CartItemDto> items = new ArrayList<>();
}
