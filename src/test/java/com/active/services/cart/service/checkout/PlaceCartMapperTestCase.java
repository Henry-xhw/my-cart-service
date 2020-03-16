package com.active.services.cart.service.checkout;

import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.domain.CartItemFeesInCart;
import com.active.services.cart.domain.Discount;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.model.FeeTransactionType;
import com.active.services.order.OrderLineType;
import com.active.services.order.OrderStatus;
import com.active.services.order.management.api.v3.types.DiscountDTO;
import com.active.services.order.management.api.v3.types.OrderDTO;
import com.active.services.order.management.api.v3.types.OrderLineDTO;
import com.active.services.order.management.api.v3.types.OrderLineFeeDTO;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PlaceCartMapperTestCase {

    @Test
    public void toOrderDTO() {
        UUID relatedIdentifier = UUID.randomUUID();
        Cart cart = CartDataFactory.placeOrderCart();

        CartItem subItem = CartDataFactory.getPlaceOrderCartItem(3, new BigDecimal(20), "child description");
        CartItem subItem2 = CartDataFactory.getPlaceOrderCartItem(4, new BigDecimal(100), "child description two");

        List<CartItemFee> fees = new ArrayList<>();
        CartItemFeesInCart subCartItemFee = CartDataFactory.getCartItemFeesInCart(FeeTransactionType.DEBIT,
                CartItemFeeType.PROCESSING_FLAT,
                3, new BigDecimal(10), "sub Description one", "sub name one", relatedIdentifier);
        CartItemFeesInCart subCartItemFee2 = CartDataFactory.getCartItemFeesInCart(FeeTransactionType.CREDIT, CartItemFeeType.PRICE,
                2, new BigDecimal(15), "sub Description two", "sub name two", relatedIdentifier);
        fees.add(subCartItemFee);
        fees.add(subCartItemFee2);

        cart.setDiscounts(Arrays.asList(CartDataFactory.getDiscount(relatedIdentifier)));

        subItem.getFees().get(0).setSubItems(fees);
        List<CartItem> subItems = new ArrayList<>();
        subItems.add(subItem);
        subItems.add(subItem2);

        cart.getItems().get(0).setSubItems(subItems);
        cart.getItems().add(CartDataFactory.placeOrderCartItem());

        OrderDTO orderDTO = PlaceCartMapper.MAPPER.toDto(cart);

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
        assertEquals(OrderTypeMapper.setFeeTransactionType(fee.getTransactionType()), orderLineFee.getFeeTransactionType());
        assertEquals(OrderTypeMapper.setFeeType(fee.getType()), orderLineFee.getFeeType());
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
