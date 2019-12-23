package com.active.services.cart.controller.v1;

import com.active.services.cart.controller.UUIDGenerator;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.v1.QuoteCartDto;
import com.active.services.cart.model.v1.QuoteCartItemDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = UUIDGenerator.class)
public interface QuoteCartMapper {

    QuoteCartMapper INSTANCE = Mappers.getMapper(QuoteCartMapper.class);

    @InheritInverseConfiguration
    QuoteCartDto toDto(Cart dto);

    @InheritInverseConfiguration
    QuoteCartItemDto toDto(CartItem dto);

}
