package com.active.services.cart.client.rest;

import com.active.services.order.management.api.v3.types.PlaceOrderReq;
import com.active.services.order.management.api.v3.types.PlaceOrderRsp;

import feign.Headers;
import feign.RequestLine;

@Headers("Content-Type: application/json")
public interface OrderService {
    @RequestLine("POST /")
    PlaceOrderRsp placeOrder(PlaceOrderReq request);
}