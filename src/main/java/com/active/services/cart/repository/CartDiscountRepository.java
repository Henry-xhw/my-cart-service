package com.active.services.cart.repository;

import com.active.services.cart.domain.CartDiscount;
import com.active.services.cart.repository.mapper.CartDiscountMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CartDiscountRepository {
    private final CartDiscountMapper cartDiscountMapper;

    public void createDiscount(CartDiscount cartDiscount) {
        cartDiscountMapper.createDiscount(cartDiscount);
    }
}
