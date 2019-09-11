package com.active.services.cart.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {
    /**
     * A specific string to mark the cart item.
     */
    @NotBlank
    @Size(min = 1, max = 255, message = "must be 1-255 chars")
    private String identifier;

    @NotNull
    private Long productId;

    private int quantity;

    private CartItemOption option;

    /**
     * It can override the cartItem's price
     */
    private BigDecimal priceOverride;

    /**
     * It will take some dynamical properties
     */
    private CartItemFacts cartItemFacts;

    /**
     * It can indicate parent-child relationships between two cartItems
     */
    private String parentIdentifier;

}
