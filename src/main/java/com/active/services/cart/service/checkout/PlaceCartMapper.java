package com.active.services.cart.service.checkout;

import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.domain.Discount;
import com.active.services.order.management.api.v3.types.DiscountDTO;
import com.active.services.order.management.api.v3.types.OrderDTO;
import com.active.services.order.management.api.v3.types.OrderLineDTO;
import com.active.services.order.management.api.v3.types.OrderLineFeeDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { OrderTypeMapper.class})
public interface PlaceCartMapper {

    PlaceCartMapper MAPPER = Mappers.getMapper(PlaceCartMapper.class);

    @Mappings({@Mapping(target = "orderStatus", expression = "java(com.active.services.order.OrderStatus.PENDING)"),
            @Mapping(target = "orderOwnerEnterprisePersonId", source = "ownerId"),
            @Mapping(target = "enterprisePersonId", source = "keyerId"),
            @Mapping(target = "orderLines", source = "items"),
            @Mapping(target = "businessDate", expression = "java(java.time.Instant.now())"),
            @Mapping(target = "referenceId", source = "identifier")
    })
    OrderDTO toOrderDTO(Cart cart);


    /*    private String groupingIdentifier;
          private UUID identifier;
        private Long glcodeId;
    */
    @Mappings({@Mapping(target = "description", source = "productDescription"),
            @Mapping(target = "childOrderLines", source = "subItems"),
            @Mapping(target = "orderLineType", expression = "java(com.active.services.order.OrderLineType.SALE)"),
            @Mapping(target = "orderLineFees", source = "fees"),
            @Mapping(target = "systemPrice", source = "grossPrice"),
            @Mapping(target = "referenceId", source = "identifier")
    })
    OrderLineDTO toLineDTO(CartItem cartItem);

    @Mappings({
            @Mapping(target = "orderLineFees", source = "subItems"),
            @Mapping(target = "amount", source = "unitPrice"),
            @Mapping(target = "feeTransactionType", source = "transactionType"),
            @Mapping(target = "entityReferenceId", source = "relatedIdentifier"),
            @Mapping(target = "feeType", source = "type")
    })
    OrderLineFeeDTO toFeeDTO(CartItemFee cartItemFee);

    DiscountDTO toDiscountDTO(Discount discount);

    default String map(java.util.UUID value) {
        if (value == null) {
            return "";
        }

        return value.toString();
    }
}