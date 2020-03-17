package com.active.services.cart.service.quote.discount.coupon;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.Discount;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
class CartItemDiscounts {

    private CartItem cartItem;

    private List<Discount> discounts;

    public BigDecimal getTotalNetPrice() {
        return cartItem.getNetPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
    }

    public List<Long> getAllDiscountIds() {
        return CollectionUtils.emptyIfNull(discounts).stream().map(Discount::getDiscountId)
                .distinct().collect(Collectors.toList());
    }

    public CartItemDiscounts filterDiscounts(List<Long> filterIds) {
        discounts =
                CollectionUtils.emptyIfNull(discounts).stream().filter(discountApplication ->
                        filterIds.contains(discountApplication.getDiscountId())).collect(Collectors.toList());
        return this;
    }
}
