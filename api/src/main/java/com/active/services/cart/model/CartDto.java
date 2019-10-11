package com.active.services.cart.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {

    private String referenceId;

    /**
     * The currency code used to represent a monetary values associated with the cart,
     * all cart items under the cart should use the same currency code.
     */
    @NotBlank
    @Size(min = 3, max = 3, message = "must be 3 chars")
    private String currency;

    private List<CartItemDto> cartItemDtos;

    /**
     * A specific pricing date for the cart
     */
    private LocalDateTime priceDate;
}
