package com.active.services.cart.service.quote.discount;

import com.active.services.cart.domain.CartItem;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CartItemDiscounts {

    private CartItem cartItem;

    private List<Discount> couponDiscounts;
}
