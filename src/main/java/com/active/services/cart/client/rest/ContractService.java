package com.active.services.cart.client.rest;

import com.active.services.contract.controller.v1.req.CalculateFeeAmountsReq;
import com.active.services.contract.controller.v1.rsp.CalculateFeeAmountsRsp;

import feign.Headers;
import feign.RequestLine;

/**
 * @author henryxu
 */
@Headers("Content-Type: application/vnd.active.contract-service.v1+json")
public interface ContractService {
    @RequestLine("POST /contracts/calculation")
    CalculateFeeAmountsRsp calculateFeeAmounts(CalculateFeeAmountsReq req);
}
