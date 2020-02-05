package com.active.services.cart.service.quote;

import com.active.services.contract.controller.v1.FeeOwner;

public interface ProcessingFeePricer {
    void quote(CartQuoteContext context, FeeOwner feeOwner);
}
