package com.active.services.cart.repository.mapper;

import com.active.services.cart.domain.Discount;
import com.active.services.product.DiscountType;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface DiscountMapper {
    void batchInsertDiscount(List<Discount> discounts);

    Optional<Discount> getDiscountByDiscountIdAndType(@Param("discountType") DiscountType discountType,
                                                      @Param("discountId") Long discountId);
}
