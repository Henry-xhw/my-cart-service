package com.active.services.cart.domain;

import com.active.platform.types.range.Range;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.model.CouponMode;
import com.active.services.cart.model.FeeTransactionType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

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

    private BigDecimal overridePrice;

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

    public Optional<CartItemFee> getPriceCartItemFee() {
        return getFees().stream()
                    .filter(f -> f.getType() == CartItemFeeType.PRICE)
                    .filter(f -> f.getTransactionType() == FeeTransactionType.DEBIT).findFirst();
    }

    public List<CartItemFee> getFlattenCartItemFees() {
        return flattenCartItemFees(fees);
    }

    public static List<CartItemFee> flattenCartItemFees(List<CartItemFee> cartItemFees) {
        Queue<CartItemFee> q = new LinkedList<>(cartItemFees);
        List<CartItemFee> flatten = new LinkedList<>();
        while (!q.isEmpty()) {
            CartItemFee it = q.poll();
            if (it != null) {
                flatten.add(it);
                emptyIfNull(it.getSubItems()).stream()
                        .filter(Objects::nonNull)
                        .forEach(q::offer);
            }
        }
        return flatten;
    }

    public BigDecimal getNetPrice() {
        BigDecimal discountAmount = emptyIfNull(getFlattenCartItemFees()).stream()
                .filter(cartItemFee -> CartItemFeeType.isDiscount(cartItemFee.getType()))
                .map(CartItemFee::getUnitPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal priceAmt = getPriceCartItemFee().map(CartItemFee::getUnitPrice).orElse(BigDecimal.ZERO);
        BigDecimal grossPriceAmt = Optional.ofNullable(getGrossPrice()).orElse(priceAmt);
        BigDecimal netPriceAmt = grossPriceAmt.subtract(discountAmount);
        return netPriceAmt.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : netPriceAmt;
    }
}
