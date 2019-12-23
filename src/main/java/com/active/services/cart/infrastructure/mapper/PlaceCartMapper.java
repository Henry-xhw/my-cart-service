package com.active.services.cart.infrastructure.mapper;

import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.order.management.api.v3.types.OrderDTO;
import com.active.services.order.management.api.v3.types.OrderLineDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlaceCartMapper {

    PlaceCartMapper MAPPER = Mappers.getMapper(PlaceCartMapper.class);

    @Mappings({@Mapping(target = "currencyCode", expression = "java(cart.getCurrencyCode().name())"),
        @Mapping(target = "orderStatus", expression = "java(com.active.services.order.OrderStatus.PENDING)"),
        @Mapping(target = "orderOwnerEnterprisePersonId", source = "ownerId"),
        @Mapping(target = "enterprisePersonId", source = "keyerId"),
        @Mapping(target = "orderLines", source = "items")
        /*
            private DateTime businessDate;

        */})
    OrderDTO toOrderDTO(Cart cart);

    @Mappings({@Mapping(target = "productId", source = "productId"),
        @Mapping(target = "description", source = "productDescription"),
        @Mapping(target = "childOrderLines", source = "subItems"),
        @Mapping(target = "orderLineType", expression = "java(com.active.services.order.OrderLineType.SALE)"),
        @Mapping(target = "price", source = "unitPrice"),


        /*    private String groupingIdentifier; from cartItem
        from OrderLineDTO:
            private Integer inventoryOverrideQuantity;
            private Integer feeVolumeIndex;
            private String orderLineFees;
            private Long glcodeId;
            private BigDecimal price;   -----need to double check.
        */
    })
    OrderLineDTO toLineDTO(CartItem cartItem);

    // OrderLineFeeDTO toFeeDTO(CartItemFee cartItemFee);
}