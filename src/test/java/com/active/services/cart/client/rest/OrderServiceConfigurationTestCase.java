package com.active.services.cart.client.rest;

import com.active.services.domain.DateTime;
import com.active.services.order.FeeTransactionType;
import com.active.services.order.OrderLineFeeType;
import com.active.services.order.OrderLineType;
import com.active.services.order.OrderStatus;
import com.active.services.order.management.api.v3.types.OrderDTO;
import com.active.services.order.management.api.v3.types.OrderLineDTO;
import com.active.services.order.management.api.v3.types.OrderLineFeeDTO;
import com.active.services.order.management.api.v3.types.PlaceOrderReq;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Ignore
public class OrderServiceConfigurationTestCase {

    @Autowired OrderService orderService;

    @Test
    public void orderService() {
        RestServiceConfiguration conf = new RestServiceConfiguration();
        ReflectionTestUtils.setField(conf, "orderServiceUrl",
            "http://127.0.0.1:8200/order-management-service/api/place-order");
        orderService = conf.orderService();
        try {
            orderService.placeOrder(getPlaceOrderReq());
        } catch (Exception e) {
            if (e instanceof FeignException.BadRequest) {
                LOG.info(((FeignException.BadRequest) e).contentUTF8());
            } else {
                LOG.info(e.getMessage());
            }
        }
    }

    private PlaceOrderReq getPlaceOrderReq() {
        // request
        PlaceOrderReq request = new PlaceOrderReq();
        OrderDTO orderDTO = getOrderDTO();
        request.setOrderDTO(orderDTO);

        // orderline
        OrderLineDTO orderLineDTO = getOrderLineDTO();
        List<OrderLineDTO> orderLineDTOS = new ArrayList<>();
        orderLineDTOS.add(orderLineDTO);
        orderDTO.setOrderLines(orderLineDTOS);

        // orderlinefee
        OrderLineFeeDTO orderLineFeeDTO = getOrderLineFeeDTO();
        List<OrderLineFeeDTO> orderLineFeeDTOS = new ArrayList<>();
        orderLineFeeDTOS.add(orderLineFeeDTO);
        orderLineDTO.setOrderLineFees(orderLineFeeDTOS);

        return request;
    }

    private OrderLineFeeDTO getOrderLineFeeDTO() {
        OrderLineFeeDTO orderLineFeeDTO = new OrderLineFeeDTO();
        orderLineFeeDTO.setAmount(new BigDecimal("10"));
        orderLineFeeDTO.setUnits(1);
        orderLineFeeDTO.setFeeTransactionType(FeeTransactionType.DEBIT);
        orderLineFeeDTO.setFeeType(OrderLineFeeType.PRICE);
        return orderLineFeeDTO;
    }

    private OrderLineDTO getOrderLineDTO() {
        OrderLineDTO orderLineDTO = new OrderLineDTO();
        orderLineDTO.setProductId(RandomUtils.nextLong());
        orderLineDTO.setOrderLineType(OrderLineType.SALE);
        orderLineDTO.setQuantity(2);
        orderLineDTO.setInventoryOverrideQuantity(0);
        return orderLineDTO;
    }

    private OrderDTO getOrderDTO() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCurrencyCode("USD");
        orderDTO.setOrderStatus(OrderStatus.PENDING);
        orderDTO.setEnterprisePersonId(UUID.randomUUID());
        orderDTO.setOrderOwnerEnterprisePersonId(UUID.randomUUID());
        orderDTO.setOrderUrl("ss.sl");
        orderDTO.setBusinessDate(Instant.now());
        return orderDTO;
    }
}
