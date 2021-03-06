package com.active.services.cart.model.v1;

import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.model.FeeTransactionType;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class QuoteCartItemFeeDto extends BaseDto {
    private Long parentId;

    private String name;

    private String description;

    @NotNull
    private CartItemFeeType type;

    @NotNull
    private FeeTransactionType transactionType;

    @NotNull
    @Min(value = 1)
    private Integer units;

    @Min(value = 0)
    @Digits(integer = 17, fraction = 2)
    private BigDecimal unitPrice;

    private BigDecimal dueAmount;

    @Valid
    private List<QuoteCartItemFeeDto> subItems = new ArrayList<>();
}
