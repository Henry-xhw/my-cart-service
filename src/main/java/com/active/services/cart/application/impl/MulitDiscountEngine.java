package com.active.services.cart.application.impl;

import com.active.services.cart.domain.cart.Cart;
import com.active.services.cart.domain.cart.CartItem;
import com.active.services.cart.infrastructure.repository.ProductRepository;
import com.active.services.product.discount.multi.MultiDiscount;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MulitDiscountEngine {
    @NonNull private final ProductRepository productRepo;

    public void apply(Cart cart) {
        for (CartItem item : cart.getCartItems()) {
            List<MultiDiscount> mds = productRepo.findEffectiveMultiDiscountsByProductId(item.getProductId(), cart.getPriceDate());

        }
    }
}
