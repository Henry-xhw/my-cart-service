package com.active.services.cart.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateCartsResp {
    private List<CartResult> cartResults;
}
