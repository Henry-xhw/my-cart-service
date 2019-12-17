package com.active.services.cart.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.active.services.cart.model.CartItemFeeTransactionType;
import com.active.services.cart.model.CartItemFeeType;

import lombok.Data;

@Data
public class CartItemFee extends BaseDomainObject {
    private Long parentId;

    private String name;

    private String description;

    private CartItemFeeType type;

    private CartItemFeeTransactionType transactionType;

    private Integer units;

    private BigDecimal unitPrice;

    private String feeOriginId;

    private String feeOriginPayload;

    private List<CartItemFee> derivedFees = new ArrayList<>();
}
