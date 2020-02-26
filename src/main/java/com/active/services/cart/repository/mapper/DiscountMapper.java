package com.active.services.cart.repository.mapper;

import com.active.services.cart.domain.Discount;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DiscountMapper {
    void createDiscount(Discount discount);

    void deletePreviousDiscountByCartId(@Param("cartId") Long cartId);
}
