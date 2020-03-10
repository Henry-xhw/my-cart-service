package com.active.services.cart.repository.mapper;

import com.active.services.cart.domain.AdHocDiscount;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdHocDiscountMapper {
    void createAdHocDiscounts(List<AdHocDiscount> adHocDiscounts);
}
