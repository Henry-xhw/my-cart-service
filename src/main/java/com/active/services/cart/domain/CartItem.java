package com.active.services.cart.domain;

import com.active.services.cart.model.Range;
import com.active.services.cart.model.v1.UpdateCartItemDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
public class CartItem extends BaseDomainObject {

    private Long productId;

    private String productName;

    private String productDescription;

    private Range<Instant> bookingRange;

    private Range<Instant> trimmedBookingRange;

    private Integer quantity;

    private BigDecimal unitPrice;

    private String groupingIdentifier;

    public CartItem(UpdateCartItemDto updateCartItemDto) {
        this.productId = updateCartItemDto.getProductId();
        this.productName = updateCartItemDto.getProductName();
        this.productDescription = updateCartItemDto.getProductDescription();
        this.bookingRange = updateCartItemDto.getBookingRange();
        this.trimmedBookingRange = updateCartItemDto.getTrimmedBookingRange();
        this.quantity = updateCartItemDto.getQuantity();
        this.unitPrice = updateCartItemDto.getUnitPrice();
        this.groupingIdentifier = updateCartItemDto.getGroupingIdentifier();
        this.setIdentifier(updateCartItemDto.getIdentifier());
    }
}
