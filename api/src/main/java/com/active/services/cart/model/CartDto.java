package com.active.services.cart.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {

    /**
     * A reference id will link cart and external system object
     */
    private String referenceId;

    /**
     * The currency code used to represent a monetary values associated with the cart,
     * all cart items under the cart should use the same currency code.
     */
    @NotBlank
    @Size(min = 3, max = 3, message = "must be 3 chars")
    private String currency;

    @Valid
    @NotEmpty
    private List<CartItemDto> cartItemDtos;

    /**
     * A specific pricing date for the cart
     */
    private LocalDateTime priceDate;
}
