package com.active.services.cart.client.rest;

import com.active.services.product.nextgen.v1.req.GetProductFeeReq;
import com.active.services.product.nextgen.v1.rsp.GetProductFeeRsp;

import feign.Headers;
import feign.RequestLine;

@Headers("Content-Type: application/vnd.active.product-service.v1+json")
public interface ProductService {
    @RequestLine("POST /api/product/fee")
    GetProductFeeRsp quote(GetProductFeeReq getProductFeeReq);
}