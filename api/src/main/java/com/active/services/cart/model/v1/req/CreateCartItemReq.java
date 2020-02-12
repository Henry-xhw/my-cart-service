package com.active.services.cart.model.v1.req;

import com.active.services.cart.model.v1.CartItemDto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Data
public class CreateCartItemReq {
    @NotEmpty
    @Valid
    private List<CartItemDto> items = new ArrayList<>();
}
