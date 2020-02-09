package com.active.services.cart.service.quote;

import com.active.services.cart.domain.Cart;
import com.active.services.product.Product;

import lombok.Data;

import java.util.List;

@Data
public class CartQuoteContext {
    private Cart cart;
    private List<Product> products;

    public CartQuoteContext(Cart cart) {
        this.cart = cart;
    }
}
