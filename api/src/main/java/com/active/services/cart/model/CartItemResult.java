package com.active.services.cart.model;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResult {

    /**
     * A specific string to mark the cart
     */
    @NotBlank
    @Size(min = 1, max = 255, message = "must be 1-255 chars")
    private String identifier;
    private String referenceId;
    private List<CartItemFee> cartItemFees;
    private BigDecimal itemTotal;
    private BigDecimal feeTotal;
    private BigDecimal taxTotal;
}
