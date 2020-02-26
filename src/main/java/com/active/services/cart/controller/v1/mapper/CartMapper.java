package com.active.services.cart.controller.v1.mapper;

import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.v1.CartDto;
import com.active.services.cart.model.v1.CartItemDto;
import com.active.services.cart.model.v1.CreateCartItemDto;
import com.active.services.cart.model.v1.UpdateCartItemDto;
import com.active.services.cart.model.v1.req.CheckoutReq;
import com.active.services.cart.model.v1.req.CreateCartReq;
import com.active.services.cart.model.v1.req.UpdateCartReq;
import com.active.services.cart.service.checkout.CheckoutContext;

import org.mapstruct.Context;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartMapper {

    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    Cart toDomain(CartDto dto, @Context boolean isCreate);

    CartItem toDomain(CartItemDto dto, @Context boolean isCreate);

    CartItem toDomain(CreateCartItemDto dto, @Context boolean isCreate);

    CartItem toDomain(UpdateCartItemDto dto);

    List<CartItem> toDomain(List<UpdateCartItemDto> dtos);

    CheckoutContext toDomain(CheckoutReq req);

    @InheritInverseConfiguration
    CartDto toDto(Cart cart);

    @InheritInverseConfiguration
    CartItemDto toDto(CartItem cartItem);

    Cart toDomainFromCreateCartReq(CreateCartReq req, @Context boolean isCreate);

    @InheritInverseConfiguration
    CreateCartReq toCreateCartReq(Cart cart);

    Cart toDomainFromUpdateCartReq(UpdateCartReq req);


}
