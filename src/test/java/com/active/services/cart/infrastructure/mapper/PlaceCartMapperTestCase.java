package com.active.services.cart.infrastructure.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.model.FeeTransactionType;
import com.active.services.order.OrderLineType;
import com.active.services.order.OrderStatus;
import com.active.services.order.management.api.v3.types.OrderDTO;
import com.active.services.order.management.api.v3.types.OrderLineDTO;
import com.active.services.order.management.api.v3.types.OrderLineFeeDTO;

public class PlaceCartMapperTestCase {

    @Test
    public void toOrderDTO() {
        Cart cart = CartDataFactory.cart();

        CartItem subItem = CartDataFactory.getCartItem(3, new BigDecimal(20), "child description");
        CartItem subItem2 = CartDataFactory.getCartItem(4, new BigDecimal(100), "child description two");

        List<CartItemFee> fees = new ArrayList<>();
        CartItemFee subCartItemFee = CartDataFactory.getCartItemFee(FeeTransactionType.DEBIT,
                CartItemFeeType.PROCESSING_FLAT,
                3, new BigDecimal(10), "sub Description one", "sub name one");
        CartItemFee subCartItemFee2 = CartDataFactory.getCartItemFee(FeeTransactionType.CREDIT, CartItemFeeType.PRICE,
                2, new BigDecimal(15), "sub Description two", "sub name two");
        fees.add(subCartItemFee);
        fees.add(subCartItemFee2);

        subItem.getFees().get(0).setSubItems(fees);
        List<CartItem> subItems = new ArrayList<>();
        subItems.add(subItem);
        subItems.add(subItem2);

        cart.getItems().get(0).setSubItems(subItems);
        cart.getItems().add(CartDataFactory.cartItem());

        OrderDTO orderDTO = PlaceCartMapper.MAPPER.toOrderDTO(cart);

        checkOrderDTO(cart, orderDTO);

        assertEquals(cart.getItems().size(), orderDTO.getOrderLines().size());
        assertEquals(3, cart.getItems().size());

        CartItem cartItem = cart.getItems().get(0);
        OrderLineDTO orderLineDTO =  orderDTO.getOrderLines().get(0);

        checkOrderLineDTO(cartItem, orderLineDTO);
        assertEquals(2, orderLineDTO.getChildOrderLines().size());

        OrderLineDTO subOrderLineDTO = orderDTO.getOrderLines().get(0).getChildOrderLines().get(0);
        OrderLineDTO subOrderLineDTO2 = orderDTO.getOrderLines().get(0).getChildOrderLines().get(1);

        checkOrderLineDTO(subItem, subOrderLineDTO);
        checkOrderLineDTO(subItem2, subOrderLineDTO2);

        assertEquals(subItem.getFees().size(), subOrderLineDTO.getOrderLineFees().size());

        CartItemFee fee = subItem.getFees().get(0);
        OrderLineFeeDTO orderLineFee = subOrderLineDTO.getOrderLineFees().get(0);

        checkOrderLineFeeDTO(fee, orderLineFee);
        assertEquals(2, orderLineFee.getOrderLineFees().size());
        checkOrderLineFeeDTO(fee.getSubItems().get(0), orderLineFee.getOrderLineFees().get(0));
        checkOrderLineFeeDTO(fee.getSubItems().get(1), orderLineFee.getOrderLineFees().get(1));
    }

    private void checkOrderLineFeeDTO(CartItemFee fee, OrderLineFeeDTO orderLineFee) {
        assertEquals(fee.getName(), orderLineFee.getName());
        assertEquals(fee.getDescription(), orderLineFee.getDescription());
        assertEquals(fee.getUnitPrice(), orderLineFee.getAmount());
        assertEquals(OrderTypeMapping.setFeeTransactionType(fee.getTransactionType()), orderLineFee.getFeeTransactionType());
        assertEquals(OrderTypeMapping.setFeeType(fee.getType()), orderLineFee.getFeeType());
        assertEquals(fee.getUnits(), orderLineFee.getUnits());
        assertEquals(fee.getSubItems().size(), orderLineFee.getOrderLineFees().size());
    }

    private void checkOrderLineDTO(CartItem cartItem, OrderLineDTO orderLineDTO) {
        assertEquals(cartItem.getProductId(), orderLineDTO.getProductId());
        assertEquals(cartItem.getProductDescription(), orderLineDTO.getDescription());
        assertEquals(OrderLineType.SALE, orderLineDTO.getOrderLineType());
        assertEquals(cartItem.getQuantity(), orderLineDTO.getQuantity());
        assertEquals(cartItem.getFeeVolumeIndex(), orderLineDTO.getFeeVolumeIndex());
        assertEquals(cartItem.getSubItems().size(), orderLineDTO.getChildOrderLines().size());
    }

    private void checkOrderDTO(Cart cart, OrderDTO orderDTO) {
        assertNotNull(orderDTO);
        assertEquals("USD", orderDTO.getCurrencyCode());
        assertEquals(OrderStatus.PENDING, orderDTO.getOrderStatus());
        assertEquals(cart.getKeyerId(), orderDTO.getEnterprisePersonId());
        assertEquals(cart.getOwnerId(), orderDTO.getOrderOwnerEnterprisePersonId());
        assertEquals(cart.getKeyerId(), orderDTO.getEnterprisePersonId());
        assertEquals(cart.getItems().size(), orderDTO.getOrderLines().size());
    }

}
