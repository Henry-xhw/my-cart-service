package com.active.services.cart.model.v1;

import java.math.BigDecimal;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.active.services.cart.model.CartItemFeeTransactionType;
import com.active.services.cart.model.CartItemFeeType;
import lombok.Data;

@Data
public class QuoteCartItemFeeDto extends BaseDto {
    private Long parentId;

    private String name;

    private String description;

    @NotNull
    private CartItemFeeType type;

    @NotNull
    private CartItemFeeTransactionType transactionType;

    @NotNull
    @Min(value = 1)
    private Integer units;

    @Min(value = 0)
    @Digits(integer = 17, fraction = 2)
    private BigDecimal unitPrice;
}
