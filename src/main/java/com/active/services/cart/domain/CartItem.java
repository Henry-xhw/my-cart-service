package com.active.services.cart.domain;

import com.active.platform.types.range.Range;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.model.CouponMode;
import com.active.services.cart.model.FeeTransactionType;
import com.active.services.cart.model.v1.UpdateCartItemDto;
import com.active.services.cart.service.quote.discount.Discount;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    private String personIdentifier;

    private List<CartItemFee> fees = new ArrayList<>();

    private Set<String> couponCodes;

    private boolean ignoreMultiDiscounts;

    private CouponMode couponMode;

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
        this.couponCodes = updateCartItemDto.getCouponCodes();
        this.couponMode = updateCartItemDto.getCouponMode();
        this.personIdentifier = updateCartItemDto.getPersonIdentifier();
        this.ignoreMultiDiscounts = updateCartItemDto.isIgnoreMultiDiscounts();
        this.couponMode = updateCartItemDto.getCouponMode();
    }

    public CartItem applyDiscount(Discount disc, String currency) {
        fees.stream()
                .filter(f -> f.getType() == CartItemFeeType.PRICE)
                .filter(f -> f.getTransactionType() == FeeTransactionType.DEBIT)
                .forEach(f -> f.applyDiscount(disc, currency));
        return this;
    }
}
