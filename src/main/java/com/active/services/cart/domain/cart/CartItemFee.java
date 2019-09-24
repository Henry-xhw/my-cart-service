package com.active.services.cart.domain.cart;

import java.math.BigDecimal;
import java.util.List;

import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.model.FeeTransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
    private BigDecimal subtotal;

    private List<CartItemFee> derivedFees;
}