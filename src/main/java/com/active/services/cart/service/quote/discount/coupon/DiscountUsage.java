package com.active.services.cart.service.quote.discount.coupon;

import lombok.Data;

@Data
public class DiscountUsage {
    private Long discountId;
    private Integer usage;
    private Integer limit;/// return by this get function or by discount search.
}
