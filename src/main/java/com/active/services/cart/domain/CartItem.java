package com.active.services.cart.domain;

import com.active.services.cart.model.Range;
import com.active.services.cart.model.v1.UpdateCartItemDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

@Data
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

    public List<CartItem> getFlattenSubItems() {
        Queue<CartItem> q = new LinkedList<>();
        q.offer(this);

        List<CartItem> flatten = new LinkedList<>();
        while (!q.isEmpty()) {
            int size = q.size();
            for (int i = 0; i < size; i++) {
                CartItem it = q.poll();
                if (it != null) {
                    flatten.add(it);
                    emptyIfNull(it.getSubItems()).stream()
                            .filter(Objects::nonNull)
                            .forEach(q::offer);
                }
            }
        }
        return flatten;
    }

    public BigDecimal getCartItemTotal() {
        return BigDecimal.ZERO;
    }
}
