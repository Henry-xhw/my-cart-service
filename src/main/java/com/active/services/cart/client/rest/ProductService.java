package com.active.services.cart.client.rest;

import com.active.services.product.nextgen.v1.req.QuoteSingleReq;
import com.active.services.product.nextgen.v1.rsp.QuoteSingleRsp;

import feign.Headers;
import feign.RequestLine;

@Headers("Content-Type: application/vnd.active.product-service.v1+json")
public interface ProductService {
    @RequestLine("POST /api/quote/single")
    QuoteSingleRsp quote(QuoteSingleReq quoteSingleReq);
}