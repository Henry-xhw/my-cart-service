package com.active.services.cart.service.checkout;

import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.domain.CartItemFeesInCart;
import com.active.services.cart.domain.Discount;
import com.active.services.order.OrderLineType;
import com.active.services.order.OrderStatus;
import com.active.services.order.management.api.v3.types.DiscountDTO;
import com.active.services.order.management.api.v3.types.OrderDTO;
import com.active.services.order.management.api.v3.types.OrderLineDTO;
import com.active.services.order.management.api.v3.types.OrderLineFeeDTO;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PlaceCartMapperTestCase {

    @Test
    public void cartToOrderDtoSuccess() {
        Cart cart = CartDataFactory.cart();
        OrderDTO orderDTO = PlaceCartMapper.MAPPER.toDto(cart);
        assertEquals(OrderStatus.PENDING, orderDTO.getOrderStatus());
        assertEquals(cart.getOwnerId(), orderDTO.getOrderOwnerEnterprisePersonId());
        assertEquals(cart.getKeyerId(), orderDTO.getEnterprisePersonId());
        assertEquals(cart.getItems().size(), orderDTO.getOrderLines().size());
        assertNotNull(orderDTO.getBusinessDate());
        assertEquals(cart.getIdentifier().toString(), orderDTO.getReferenceId().toString());
    }

    @Test
    public void cartItemToOrderLineDTOSuccess() {
        CartItem cartItem = CartDataFactory.cartItem();
        OrderLineDTO orderLineDTO = PlaceCartMapper.MAPPER.toDto(cartItem);
        assertEquals(cartItem.getProductDescription(), orderLineDTO.getDescription());
        assertEquals(cartItem.getSubItems(), orderLineDTO.getChildOrderLines());
        assertEquals(OrderLineType.SALE, orderLineDTO.getOrderLineType());
        assertEquals(cartItem.getGrossPrice(), orderLineDTO.getSystemPrice());
        assertEquals(cartItem.getIdentifier().toString(), orderLineDTO.getReferenceId());
        assertEquals(cartItem.getPersonIdentifier(), orderLineDTO.getPersonKey());
    }

    @Test
    public void cartItemFeesInCartToOrderLineFeeDTOSuccess() {
        CartItemFeesInCart cartItemFeesInCart = CartDataFactory.cartItemFeesInCart(BigDecimal.TEN);
        cartItemFeesInCart.setRelatedIdentifier(UUID.randomUUID());
        OrderLineFeeDTO orderLineFeeDTO = PlaceCartMapper.MAPPER.toDto(cartItemFeesInCart);
        assertEquals(cartItemFeesInCart.getSubItems(), orderLineFeeDTO.getOrderLineFees());
        assertEquals(cartItemFeesInCart.getUnitPrice(), orderLineFeeDTO.getAmount());
        assertEquals(cartItemFeesInCart.getTransactionType().toString(), orderLineFeeDTO.getFeeTransactionType()
                .toString());
        assertEquals(cartItemFeesInCart.getRelatedIdentifier().toString(), orderLineFeeDTO.getEntityReferenceId());
        assertEquals(cartItemFeesInCart.getType().toString(), orderLineFeeDTO.getFeeType().toString());
    }

    @Test
    public void cartItemFeeToOrderLineFeeDTOSuccess() {
        CartItemFee cartItemFee = CartDataFactory.cartItemFee();
        cartItemFee.setRelatedIdentifier(UUID.randomUUID());
        OrderLineFeeDTO orderLineFeeDTO = PlaceCartMapper.MAPPER.toDto(cartItemFee);
        assertEquals(cartItemFee.getSubItems(), orderLineFeeDTO.getOrderLineFees());
        assertEquals(cartItemFee.getUnitPrice(), orderLineFeeDTO.getAmount());
        assertEquals(cartItemFee.getTransactionType().toString(), orderLineFeeDTO.getFeeTransactionType().toString());
        assertEquals(cartItemFee.getRelatedIdentifier().toString(), orderLineFeeDTO.getEntityReferenceId());
        assertEquals(cartItemFee.getType().toString(), orderLineFeeDTO.getFeeType().toString());
    }

    @Test
    public void discountToDiscountDTOSuccess() {
        Discount discount = CartDataFactory.getDiscount(UUID.randomUUID());
        DiscountDTO discountDTO = PlaceCartMapper.MAPPER.toDto(discount);
        assertEquals(discount.getDiscountId(), discountDTO.getId());
    }

    @Test
    public void mapSuccess() {
        UUID uuid = UUID.randomUUID();
        assertEquals(uuid.toString(), PlaceCartMapper.MAPPER.map(uuid));
    }

    @Test
    public void mapSuccessWhenUUIDIsNull() {
        assertEquals("", PlaceCartMapper.MAPPER.map(null));
    }
}
