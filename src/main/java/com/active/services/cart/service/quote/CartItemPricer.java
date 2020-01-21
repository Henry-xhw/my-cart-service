package com.active.services.cart.service.quote;

import com.active.services.cart.domain.CartItem;
import com.active.services.product.nextgen.v1.dto.fee.FeeDto;

import java.util.Map;

public interface CartItemPricer {
    void quote(CartQuoteContext context, CartItem cartItem, Map<Long, FeeDto> feeDtoHashMap);
}
