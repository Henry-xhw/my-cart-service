package com.active.services.cart.domain;

import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.model.FeeTransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CartItemFee extends BaseTree<CartItemFee> {
    private String name;

    private String description;

    private CartItemFeeType type;

    private FeeTransactionType transactionType;

    private Integer units;

    private BigDecimal unitPrice;

    private Long cartDiscountId;

    public CartItemFee refreshUnitPriceByDiscAmt(BigDecimal amt) {
        unitPrice = unitPrice.subtract(amt);
        return this;
    }

    public CartItemFee addSubItemFee(List<CartItemFee> cartItemFeeList) {

        if (getSubItems() == null) {
            setSubItems(new ArrayList<>());
        }
        getSubItems().addAll(cartItemFeeList);
        return this;
    }
}
