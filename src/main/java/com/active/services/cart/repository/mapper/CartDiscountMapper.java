package com.active.services.cart.repository.mapper;

import com.active.services.cart.domain.CartDiscount;
import com.active.services.product.DiscountType;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface CartDiscountMapper {
    void createDiscount(CartDiscount cartDiscount);

    Optional<CartDiscount> getCartDiscountByDiscountIdAndType(@Param("discountType") DiscountType discountType,
                                                             @Param("discountId") Long discountId);
}
