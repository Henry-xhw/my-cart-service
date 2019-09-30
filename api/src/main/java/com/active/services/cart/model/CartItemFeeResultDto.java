package com.active.services.cart.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemFeeResultDto {
    private String name;
    private String description;
    private CartItemFeeType feeType;
    private FeeTransactionType transactionType;
    private String cartItemFeeOrigin;
    private BigDecimal unitPrice;
    private Integer units;
    private BookingDuration duration;
    private BigDecimal subtotal;

    private List<CartItemFeeResultDto> derivedFeeResults;
}
