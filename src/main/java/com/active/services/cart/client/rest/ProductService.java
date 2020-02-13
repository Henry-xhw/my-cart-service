package com.active.services.cart.client.rest;

import com.active.services.product.nextgen.v1.req.QuoteReq;
import com.active.services.product.nextgen.v1.rsp.QuoteRsp;

import feign.Headers;
import feign.RequestLine;

@Headers("Content-Type: application/vnd.active.product-service.v1+json")
public interface ProductService {
    @RequestLine("POST /api/products/quotation")
    QuoteRsp quote(QuoteReq quoteReq);



}