package com.active.services.cart.client.rest;

import com.active.services.product.nextgen.v1.req.GetDiscountUsageReq;
import com.active.services.product.nextgen.v1.req.QuoteReq;
import com.active.services.product.nextgen.v1.req.UpdateDiscountUsageReq;
import com.active.services.product.nextgen.v1.rsp.GetDiscountUsageRsp;
import com.active.services.product.nextgen.v1.rsp.QuoteRsp;

import feign.Headers;
import feign.RequestLine;

@Headers("Content-Type: application/vnd.active.product-service.v1+json")
public interface ProductService {
    @RequestLine("POST /api/products/quotation")
    QuoteRsp quote(QuoteReq quoteReq);

    @RequestLine("POST /api/products/discounts")
    GetDiscountUsageRsp getDiscountUsages(GetDiscountUsageReq getDiscountUsageReq);

    @RequestLine("PUT /api/products/discounts")
    void updateDiscountUsages(UpdateDiscountUsageReq updateDiscountUsageReq);
}