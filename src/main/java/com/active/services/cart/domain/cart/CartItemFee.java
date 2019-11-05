package com.active.services.cart.domain.cart;

import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.model.FeeTransactionType;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Data
@Builder
public class CartItemFee {
    private Long id;
    private String name;
    private String description;
    private CartItemFeeType feeType;
    private FeeTransactionType transactionType;
    private String cartItemFeeOrigin;
    private BigDecimal unitPrice;
    private Integer units;

    private List<CartItemFee> derivedFees;

    public BigDecimal getPriceTotal() {
        return getTotal(f -> f.getFeeType() == CartItemFeeType.PRICE);
    }

    public BigDecimal getSubtotal() {
        return getTotal(f -> true);
    }

    private BigDecimal getTotal(Predicate<CartItemFee> filter) {
        List<CartItemFee> flatten = new ArrayList<>();
        populateDerivedFees(flatten, this, filter);

        return flatten.stream()
                .map(f -> BigDecimal.valueOf(units).multiply(f.getTransactionType() == FeeTransactionType.CREDIT ?
                        f.getUnitPrice() :
                        f.getUnitPrice().negate()))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private void populateDerivedFees(List<CartItemFee> flatten, CartItemFee fee, Predicate<CartItemFee> filter) {
        if (fee == null) {
            return;
        }
        if (filter.test(fee)) {
            flatten.add(fee);
        }
        if (CollectionUtils.isEmpty(fee.getDerivedFees())) {
            return;
        }
        for (CartItemFee f : fee.getDerivedFees()) {
            populateDerivedFees(flatten, f, filter);
        }
    }
}
