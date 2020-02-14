package com.active.services.cart.repository;

import com.active.services.cart.domain.Discount;
import com.active.services.cart.repository.mapper.DiscountMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DiscountRepository {
    private final DiscountMapper discountMapper;

    public void createDiscount(Discount discount) {
        discountMapper.createDiscount(discount);
    }
}
