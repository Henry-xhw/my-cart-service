package com.active.services.cart.application.impl;

import com.active.services.cart.application.CartItemSelector;
import com.active.services.cart.domain.cart.Cart;
import com.active.services.cart.domain.cart.CartItem;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class FlattenCartItemSelector implements CartItemSelector {
    @Override
    public List<CartItem> select(Cart cart) {
        if (cart == null || cart.getCartItems() == null) {
            return Collections.emptyList();
        }
        List<CartItem> res = new ArrayList<>();
        for (CartItem item : cart.getCartItems()) {
            populateChildItems(item, res);
        }
        return res;
    }

    private void populateChildItems(CartItem item, List<CartItem> res) {
        if (item == null) {
            return;
        }
        res.add(item);

        if (CollectionUtils.isEmpty(item.getCartItems())) {
            return;
        }
        for (CartItem it : item.getCartItems()) {
            res.add(it);
            populateChildItems(it, res);
        }
    }
}
