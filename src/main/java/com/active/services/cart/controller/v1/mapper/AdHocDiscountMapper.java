package com.active.services.cart.controller.v1.mapper;

import com.active.services.cart.domain.AdHocDiscount;
import com.active.services.cart.model.AdHocDiscountDto;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdHocDiscountMapper {
    List<AdHocDiscount> toDomain(List<AdHocDiscountDto> adHocDiscounts);

    List<AdHocDiscountDto> toDto(List<AdHocDiscount> adHocDiscounts);
}
