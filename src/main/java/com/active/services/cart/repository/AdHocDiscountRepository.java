package com.active.services.cart.repository;

import com.active.services.cart.domain.AdHocDiscount;
import com.active.services.cart.repository.mapper.AdHocDiscountMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AdHocDiscountRepository {

    private final AdHocDiscountMapper adHocDiscountMapper;

    public void createAdHocDiscounts(List<AdHocDiscount> adHocDiscounts) {
        adHocDiscountMapper.createAdHocDiscounts(adHocDiscounts);
    }
}
