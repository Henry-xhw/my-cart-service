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

    private String referenceId;

    /**
     * A organization identifier, it can be a agencyId, and so on.
     */
    @NotBlank
    private String orgIdentifier;

    @NotNull
    private Long productId;

    private Integer quantity;

    private List<BookingDuration> bookingDurations;
    /**
     * It can override the cartItem's price
     */
    private BigDecimal priceOverride;

    /**
     * It will take some dynamical properties
     */
    private CartItemFacts cartItemFacts;

    /**
     * It can indicate parent-child relationships
     */
    private List<CartItemDto> cartItemDtos;

}
