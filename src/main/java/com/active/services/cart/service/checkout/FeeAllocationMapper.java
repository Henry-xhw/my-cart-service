package com.active.services.cart.service.checkout;

import com.active.services.cart.model.CartItemFeeAllocation;
import com.active.services.order.management.api.v3.types.OrderLineFeeAllocationDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = UUIDToString.class)
public interface FeeAllocationMapper {

    FeeAllocationMapper MAPPER = Mappers.getMapper(FeeAllocationMapper.class);

    @Mappings({@Mapping(target = "feeReferenceId", source = "cartItemFeeIdentifier")
    })
    OrderLineFeeAllocationDTO convert(CartItemFeeAllocation feeAllocation);

    List<OrderLineFeeAllocationDTO> convert(List<CartItemFeeAllocation> feeAllocations);
}
