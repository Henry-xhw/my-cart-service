package com.active.services.cart.domain;

import com.active.platform.types.range.Range;
import com.active.services.cart.model.v1.UpdateCartItemDto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class CartItem extends BaseTree<CartItem> {

    private Long productId;

    private String productName;

    private String productDescription;

    private Range<Instant> bookingRange;

    private Range<Instant> trimmedBookingRange;

    private Integer quantity;

    private BigDecimal unitPrice;

    private String groupingIdentifier;

    private BigDecimal grossPrice;

    private BigDecimal netPrice;

    private Integer feeVolumeIndex;

    private boolean oversold;

    private List<CartItemFee> fees = new ArrayList<>();

    public CartItem(UpdateCartItemDto updateCartItemDto) {
        this.productId = updateCartItemDto.getProductId();
        this.productName = updateCartItemDto.getProductName();
        this.productDescription = updateCartItemDto.getProductDescription();
        this.bookingRange = updateCartItemDto.getBookingRange();
        this.trimmedBookingRange = updateCartItemDto.getTrimmedBookingRange();
        this.quantity = updateCartItemDto.getQuantity();
        this.unitPrice = updateCartItemDto.getUnitPrice();
        this.groupingIdentifier = updateCartItemDto.getGroupingIdentifier();
        this.feeVolumeIndex = updateCartItemDto.getFeeVolumeIndex();
        this.setIdentifier(updateCartItemDto.getIdentifier());
        this.oversold = updateCartItemDto.isOversold();
    }
}
