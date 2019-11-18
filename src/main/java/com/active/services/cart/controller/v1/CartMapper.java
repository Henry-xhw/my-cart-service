package com.active.services.cart.controller.v1;

import com.active.services.cart.controller.UUIDGenerator;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.v1.CartDto;
import com.active.services.cart.model.v1.CartItemDto;
import org.mapstruct.Context;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = UUIDGenerator.class)
public interface CartMapper {

    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    Cart toDomain(CartDto dto, @Context boolean isCreate);

    @InheritInverseConfiguration
    CartDto toDto(Cart dto);

    CartItem toDomain(CartItemDto dto, @Context boolean isCreate);

    @InheritInverseConfiguration
    CartItemDto toDto(CartItem dto);
}
