package com.active.services.cart.repository;

import com.active.services.cart.domain.Discount;
import com.active.services.cart.repository.mapper.DiscountMapper;
import com.active.services.product.DiscountType;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DiscountRepository {
    private final DiscountMapper discountMapper;

    public void batchInsertDiscount(List<Discount> discounts) {
        discountMapper.batchInsertDiscount(discounts);
    }

    public Optional<Discount> getDiscountByDiscountIdAndType(DiscountType discountType,
                                                      Long discountId) {
        return discountMapper.getDiscountByDiscountIdAndType(discountType, discountId);
    }
}
