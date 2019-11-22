package com.active.services.cart.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.active.services.cart.model.CurrencyCode;

public class Cart extends BaseDomainObject {

    private UUID ownerId;

    private UUID keyerId;

    private CurrencyCode currencyCode;

    private List<CartItem> items = new ArrayList<>();

    public Cart() {
    }

    public UUID getOwnerId() {
        return this.ownerId;
    }

    public UUID getKeyerId() {
        return this.keyerId;
    }

    public CurrencyCode getCurrencyCode() {
        return this.currencyCode;
    }

    public List<CartItem> getItems() {
        return this.items;
    }

    public Cart setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public Cart setKeyerId(UUID keyerId) {
        this.keyerId = keyerId;
        return this;
    }

    public Cart setCurrencyCode(CurrencyCode currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }

    public Cart setItems(List<CartItem> items) {
        this.items = items;
        return this;
    }

    public boolean equals(final Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Cart))
            return false;
        final Cart other = (Cart) o;
        if (!other.canEqual((Object) this))
            return false;
        final Object this$ownerId = this.getOwnerId();
        final Object other$ownerId = other.getOwnerId();
        if (this$ownerId == null ? other$ownerId != null : !this$ownerId.equals(other$ownerId))
            return false;
        final Object this$keyerId = this.getKeyerId();
        final Object other$keyerId = other.getKeyerId();
        if (this$keyerId == null ? other$keyerId != null : !this$keyerId.equals(other$keyerId))
            return false;
        final Object this$currencyCode = this.getCurrencyCode();
        final Object other$currencyCode = other.getCurrencyCode();
        if (this$currencyCode == null ? other$currencyCode != null : !this$currencyCode.equals(other$currencyCode))
            return false;
        final Object this$items = this.getItems();
        final Object other$items = other.getItems();
        if (this$items == null ? other$items != null : !this$items.equals(other$items))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Cart;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $ownerId = this.getOwnerId();
        result = result * PRIME + ($ownerId == null ? 43 : $ownerId.hashCode());
        final Object $keyerId = this.getKeyerId();
        result = result * PRIME + ($keyerId == null ? 43 : $keyerId.hashCode());
        final Object $currencyCode = this.getCurrencyCode();
        result = result * PRIME + ($currencyCode == null ? 43 : $currencyCode.hashCode());
        final Object $items = this.getItems();
        result = result * PRIME + ($items == null ? 43 : $items.hashCode());
        return result;
    }

    public String toString() {
        return "Cart(ownerId=" + this.getOwnerId() + ", keyerId=" + this.getKeyerId() + ", currencyCode=" + this
            .getCurrencyCode() + ", items=" + this.getItems() + ")";
    }
}
