package com.active.services.cart.service.quote.discount;

import com.active.services.cart.domain.Discount;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.domain.DateTime;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.util.Optional;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DiscountMapper {

    DiscountMapper MAPPER = Mappers.getMapper(DiscountMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "identifier", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "discountId", source = "id")
    @Mapping(target = "discountType", expression = "java(com.active.services.product.DiscountType.COUPON)")
    @Mapping(target = "algorithm", source = "discountAlgorithm")
    @Mapping(target = "cartId", expression = "java(context.getCart().getId())")
    Discount toDiscount(com.active.services.product.Discount disc,
                                   @Context CartQuoteContext context);

    default Instant toInstant(DateTime dateTime) {
        return Optional.ofNullable(dateTime).map(dt -> dt.toDate().toInstant()).orElse(null);
    }
}