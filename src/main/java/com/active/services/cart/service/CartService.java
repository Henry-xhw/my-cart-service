package com.active.services.cart.service;

import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.BaseTree;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.ErrorCode;
import com.active.services.cart.repository.CartRepository;
import com.active.services.cart.util.TreeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Transactional
    public void create(Cart cart) {
        cartRepository.createCart(cart);
    }

    @Transactional
    public void delete(Long cartId) {
        cartRepository.deleteCart(cartId);
    }

    @Transactional
    public Cart get(UUID cartId) {
        Cart cart = cartRepository.getCart(cartId).orElseThrow(() -> new CartException(ErrorCode.CART_NOT_FOUND,
                " cart id does not exist: " + cartId));
        TreeBuilder<CartItem> treeBuilder = new TreeBuilder(cart.getItems());
        cart.setItems(treeBuilder.buildTree());
        return cart;
    }

    @Transactional
    public List<CartItem> createCartItems(Long cartId, List<CartItem> items) {
        cartRepository.createCartItems(cartId, items);
        return items;
    }

    public long createCartItem(Long cartId, BaseTree<CartItem> cartItem) {
        return cartRepository.createCartItem(cartId, cartItem);
    }

    @Transactional
    public List<CartItem> updateCartItems(UUID cartIdentifier, List<CartItem> items) {
        checkCartItem(cartIdentifier, items.stream().map(CartItem::getIdentifier).collect(Collectors.toList()));
        cartRepository.updateCartItems(items);
        return items;
    }

    public void deleteCartItem(Cart cart, UUID cartItemId) {
        if (!isCartItemExist(cart.getItems(), cartItemId, new ArrayList<>())) {
            throw new CartException(ErrorCode.VALIDATION_ERROR, " cart item id: "
                    + cartItemId + " is not belong cart id:" + cart.getIdentifier());
        }
        Long cartId = getCartItem(cartItemId);
        cartRepository.deleteCartItem(cartId);
    }

    private void checkCartItem(UUID cartId, List<UUID> itemIds) {
        Cart cart = get(cartId);

        for (UUID itemId : itemIds) {
            if (!cart.getCartItem(itemId).isPresent()) {
                throw new CartException(ErrorCode.CART_NOT_FOUND, "cart item does not exist: " + itemId);
            }
        }
    }

    @Transactional
    public List<UUID> search(UUID ownerId) {
        List<UUID> uuidList = cartRepository.search(ownerId);
        return uuidList;
    }

    public Long getCartItem(UUID cartItemId) {
        return cartRepository.getCartItem(cartItemId).orElseThrow(() -> new CartException(ErrorCode.CART_ITEM_NOT_FOUND,
                " cartItem id: " + cartItemId));
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
                    throw new CartException(ErrorCode.VALIDATION_ERROR, " cart item id: "
                            + cartItem.getIdentifier() + " is not belong cart id:" + cart.getIdentifier());
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