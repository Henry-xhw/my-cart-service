package com.active.services.cart.service;

import com.active.services.cart.common.OperationResultCode;
import com.active.services.cart.common.exception.CartException;
import com.active.services.cart.domain.BaseTree;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.repository.CartRepository;
import com.active.services.cart.util.TreeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public void create(Cart cart) {
        cartRepository.createCart(cart);
    }

    public void delete(Long cartId) {
        cartRepository.deleteCart(cartId);
    }

    public Cart get(UUID cartId) {
        Cart cart = cartRepository.getCart(cartId).orElseThrow(() -> new CartException(OperationResultCode.CART_NOT_EXIST.getCode(),
                OperationResultCode.CART_NOT_EXIST.getDescription() + " cart id: " + cartId));
        TreeBuilder<CartItem> treeBuilder = new TreeBuilder(cart.getItems());
        cart.setItems(treeBuilder.buildTree());
        return cart;
    }

    public List<CartItem> createCartItems(Long cartId, List<CartItem> items) {
        cartRepository.createCartItems(cartId, items);
        return items;
    }

    public long createCartItem(Long cartId, BaseTree<CartItem> cartItem) {
        return cartRepository.createCartItem(cartId, cartItem);
    }

    public List<CartItem> updateCartItems(List<CartItem> items) {
        cartRepository.updateCartItems(items);
        return items;
    }

    public void deleteCartItem(Cart cart, UUID cartItemId) {
        if (!isCartItemExist(cart.getItems(), cartItemId, new ArrayList<>())) {
            throw new CartException(OperationResultCode.CART_ITEM_NOT_EXIST.getCode(),
                    OperationResultCode.CART_ITEM_NOT_EXIST.getDescription()
                            + " cart id: " + cart.getIdentifier()
                            + " cart item id: " + cartItemId);
        }
        Long cartId = getCartItem(cartItemId);
        cartRepository.deleteCartItem(cartId);
    }

    public List<UUID> search(UUID ownerId) {
        List<UUID> uuidList = cartRepository.search(ownerId);
        if (uuidList.size() == 0) {
            throw new CartException(OperationResultCode.CART_NOT_EXIST.getCode(),
                    OperationResultCode.CART_NOT_EXIST.getDescription());
        }
        return uuidList;
    }

    public Long getCartItem(UUID cartItemId) {
        return cartRepository.getCartItem(cartItemId).orElseThrow(() -> new CartException(OperationResultCode.CART_ITEM_NOT_EXIST.getCode(),
                OperationResultCode.CART_NOT_EXIST.getDescription() + " cartItem id: " + cartItemId));
    }

    private boolean isCartItemExist(List<CartItem> items, UUID cartItemId, List<UUID> cartItemIds) {
        for (CartItem cartItem : items) {
            cartItemIds.add(cartItem.getIdentifier());
            if (cartItem.getSubItems().size() > 0) {
                isCartItemExist(cartItem.getSubItems(), cartItemId, cartItemIds);
            }
        }
        return cartItemIds.contains(cartItemId);
    }

    public void insertCartItems(Cart cart, List<CartItem> cartItemList, Long pid) {
        Long cartId = cart.getId();
        for (CartItem cartItem : cartItemList) {
            Long parentId = pid;
            if (cartItem.getIdentifier() != null) {
                if (!isCartItemExist(cart.getItems(), cartItem.getIdentifier(), new ArrayList<>())) {
                    // cart item not exist, need error msg
                    throw new CartException(OperationResultCode.CART_ITEM_NOT_EXIST.getCode(),
                            OperationResultCode.CART_ITEM_NOT_EXIST.getDescription()
                                    + " cart id: " + cart.getIdentifier()
                                    + " cart item id: " + cartItem.getIdentifier());
                }
                parentId = getCartItem(cartItem.getIdentifier());
            } else {
                cartItem.setIdentifier(UUID.randomUUID());
                cartItem.setPid(parentId);
                parentId = createCartItem(cartId, cartItem);
            }
            List<CartItem> childRen = cartItem.getSubItems();
            if (childRen.size() > 0) {
                insertCartItems(cart, childRen, parentId);
            }
        }
    }
}
