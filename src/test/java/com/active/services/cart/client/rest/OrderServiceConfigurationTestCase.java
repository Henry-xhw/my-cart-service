package com.active.services.cart.client.rest;

import com.active.services.order.management.api.v3.types.OrderDTO;
import com.active.services.order.management.api.v3.types.PlaceOrderReq;
import com.active.services.order.management.api.v3.types.PlaceOrderRsp;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

@Slf4j
public class OrderServiceConfigurationTestCase {

    @Autowired OrderService orderService;

    @Test
    public void orderService() {
        OrderServiceConfiguration conf = new OrderServiceConfiguration();
        ReflectionTestUtils.setField(conf, "orderServiceUrl",
            "http://127.0.0.1:8200/order-management-service/api/place-order");
        orderService = conf.orderService();
        PlaceOrderReq req = new PlaceOrderReq();
        req.setOrderDTO(new OrderDTO());
        try {
            orderService.placeOrder(req);
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }
}
