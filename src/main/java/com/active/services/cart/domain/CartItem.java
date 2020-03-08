package com.active.services.cart.domain;

import com.active.platform.types.range.Range;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.model.CouponMode;
import com.active.services.cart.model.FeeTransactionType;
import com.active.services.cart.util.TreeBuilder;

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
import java.util.UUID;

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

    private UUID reservationId;

    // TODO
    private Long membershipId;

    public Optional<CartItemFee> getPriceCartItemFee() {
        return getFees().stream()
                    .filter(f -> f.getType() == CartItemFeeType.PRICE)
                    .filter(f -> f.getTransactionType() == FeeTransactionType.DEBIT).findFirst();
    }

    public List<CartItemFee> getFlattenCartItemFees() {
        return flattenCartItemFees(fees);
    }

    public CartItem setUnflattenItemFees(List<CartItemFee> unflattenCartItemFees) {
        TreeBuilder<CartItemFee> baseTreeTreeBuilder = new TreeBuilder<>(unflattenCartItemFees);
        fees = baseTreeTreeBuilder.buildTree();
        return this;
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
        Optional<CartItemFee> priceFee = this.getPriceCartItemFee();
        if (!priceFee.isPresent()) {
            return BigDecimal.ZERO;
        }

        BigDecimal discountAmount = emptyIfNull(priceFee.get().getSubItems()).stream()
                .filter(cartItemFee -> CartItemFeeType.isPriceDiscount(cartItemFee.getType()))
                .map(CartItemFee::getUnitPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return priceFee.get().getUnitPrice().subtract(discountAmount);
    }

    public BigDecimal getActiveProcessingFeeTotal() {
        Optional<CartItemFee> priceFee = this.getPriceCartItemFee();
        if (!priceFee.isPresent()) {
            return BigDecimal.ZERO;
        }
        return emptyIfNull(priceFee.get().getSubItems()).stream()
                .filter(cartItemFee -> CartItemFeeType.isActiveProcessingFee(cartItemFee.getType()))
                .map(cartItemFee -> cartItemFee.getTransactionType() == FeeTransactionType.DEBIT ?
                        cartItemFee.getUnitPrice() : cartItemFee.getUnitPrice().negate())
                .reduce(BigDecimal.ZERO, BigDecimal::add).multiply(new BigDecimal(quantity));
    }
}
