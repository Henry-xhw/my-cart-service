package com.active.services.cart.application.impl;

import com.active.services.cart.application.CartItemSelector;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class FlattenCartItemSelector implements CartItemSelector {
    @Override
    public List<CartItem> select(Cart cart) {
        if (cart == null || cart.getFlattenCartItems() == null) {
            return Collections.emptyList();
        }
        List<CartItem> res = new ArrayList<>();
        for (CartItem item : cart.getFlattenCartItems()) {
            populateChildItems(item, res);
        }
        return res;
    }

    private void populateChildItems(CartItem item, List<CartItem> res) {
        if (item == null) {
            return;
        }
        res.add(item);

        if (CollectionUtils.isEmpty(item.getSubItems())) {
            return;
        }
        for (CartItem it : item.getSubItems()) {
            res.add(it);
            populateChildItems(it, res);
        }
    }
}
