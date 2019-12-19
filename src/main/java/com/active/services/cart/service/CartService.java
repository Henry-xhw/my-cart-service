package com.active.services.cart.service;

import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.BaseTree;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.ErrorCode;
import com.active.services.cart.repository.CartRepository;
import com.active.services.cart.util.AuditorAwareUtil;
import com.active.services.cart.util.TreeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    private static final int UPDATE_SUCCESS = 1;

    @Transactional
    public void create(Cart cart) {
        cartRepository.createCart(cart);
    }

    @Transactional
    public void delete(Long cartId) {
        cartRepository.deleteCart(cartId);
    }

    public Cart getCartByCartUuid(UUID cartId) {
        Cart cart = cartRepository.getCart(cartId).orElseThrow(() -> new CartException(ErrorCode.CART_NOT_FOUND,
                " cart id does not exist: " + cartId));
        TreeBuilder<CartItem> treeBuilder = new TreeBuilder(cart.getItems());
        cart.setItems(treeBuilder.buildTree());
        return cart;
    }

    @Transactional
    public List<CartItem> createCartItems(Long cartId, UUID cartIdentifier, List<CartItem> items) {
        cartRepository.createCartItems(cartId, items);
        incrementVersion(cartIdentifier);
        return items;
    }

    @Transactional
    public long createCartItem(Long cartId, BaseTree<CartItem> cartItem) {
        return cartRepository.createCartItem(cartId, cartItem);
    }

    @Transactional
    public List<CartItem> updateCartItems(UUID cartIdentifier, List<CartItem> items) {
        Cart cart = getCartByCartUuid(cartIdentifier);
        items.forEach(cartItem -> {
            if (!checkCartItemIsBelongToCart(cart, cartItem.getIdentifier())) {
                throw new CartException(ErrorCode.VALIDATION_ERROR, " cart item id: "
                        + cartItem.getIdentifier() + " is not belong cart id:" + cart.getIdentifier());
            }
        });
        cartRepository.updateCartItems(items);
        incrementVersion(cartIdentifier);
        return items;
    }

    @Transactional
    public void deleteCartItem(Cart cart, UUID cartItemUuid) {
        if (!checkCartItemIsBelongToCart(cart, cartItemUuid)) {
            throw new CartException(ErrorCode.VALIDATION_ERROR, " cart item id: "
                    + cartItemUuid + " is not belong cart id:" + cart.getIdentifier());
        }
        TreeBuilder<CartItem> treeBuilder = new TreeBuilder(cart.getItems());
        cart.setItems(treeBuilder.buildTree());
        //if the cartItemUuid has sub items, add them
        List<UUID> deleteCartItemListIds = new ArrayList<>(getSubCartItemId(cart, cartItemUuid));
        //add itself
        deleteCartItemListIds.add(cartItemUuid);

        cartRepository.batchDeleteCartItems(deleteCartItemListIds);
    }

    private Set<UUID> getSubCartItemId(Cart cart, UUID cartItemUuid) {
        Set<UUID> uuidSet = new HashSet<>();
        for (CartItem cartItem: cart.getItems()) {
            if (cartItem.getIdentifier().equals(cartItemUuid)) {
                uuidSet = getAllCartItemIds(cartItem.getSubItems());
                break;
            }
        }
        return uuidSet;
    }

    public List<UUID> search(UUID ownerId) {
        List<UUID> uuidList = cartRepository.search(ownerId);
        return uuidList;
    }

    public Long getCartItemIdByCartItemUuid(UUID cartItemId) {
        return cartRepository.getCartItemIdByCartItemUuid(cartItemId)
                .orElseThrow(() -> new CartException(ErrorCode.CART_ITEM_NOT_FOUND, " cartItem id: " + cartItemId));
    }

    private boolean checkCartItemIsBelongToCart(Cart cart, UUID cartItemUuid) {
        Set cartItemIds = getAllCartItemIds(cart.getItems());
        return cartItemIds.contains(cartItemUuid);
    }

    private Set<UUID> getAllCartItemIds(List<CartItem> cartItems) {
        Queue<CartItem> q = new LinkedList<>(cartItems);
        Set<UUID> cartItemIds = new HashSet<>();
        while (!q.isEmpty()) {
            int size = q.size();
            for (int i = 0; i < size; i++) {
                CartItem it = q.poll();
                cartItemIds.add(it.getIdentifier());
                it.getSubItems().forEach(q::offer);
            }
        }
        return cartItemIds;
    }

    public void insertCartItems(Cart cart, List<CartItem> cartItemList, Long requestParentId) {
        Long cartId = cart.getId();
        for (CartItem cartItem : cartItemList) {
            Long parentId = requestParentId;
            if (cartItem.getIdentifier() != null) {
                if (!checkCartItemIsBelongToCart(cart, cartItem.getIdentifier())) {
                    // cart item not exist, need error msg
                    throw new CartException(ErrorCode.VALIDATION_ERROR, " cart item id: "
                            + cartItem.getIdentifier() + " is not belong cart id:" + cart.getIdentifier());
                }
                parentId = getCartItemIdByCartItemUuid(cartItem.getIdentifier());
            } else {
                cartItem.setIdentifier(UUID.randomUUID());
                cartItem.setParentId(parentId);
                parentId = createCartItem(cartId, cartItem);
            }
            List<CartItem> subItems = cartItem.getSubItems();
            if (subItems.size() > 0) {
                insertCartItems(cart, subItems, parentId);
            }
        }
    }

    @Transactional
    public boolean finalizeCart(UUID cartId) {
         return cartRepository.finalizeCart(cartId, AuditorAwareUtil.getAuditor()) == UPDATE_SUCCESS;
    }

    @Transactional
    public boolean incrementVersion(UUID cartId) {
        return cartRepository.incrementVersion(cartId, AuditorAwareUtil.getAuditor()) == UPDATE_SUCCESS;
    }

    @Transactional
    public boolean incrementPriceVersion(UUID cartId) {
        return cartRepository.incrementPriceVersion(cartId, AuditorAwareUtil.getAuditor()) == UPDATE_SUCCESS;
    }

    @Transactional
    public boolean acquireLock(UUID cartId) {
        return cartRepository.acquireLock(cartId, AuditorAwareUtil.getAuditor()) == UPDATE_SUCCESS;
    }

    @Transactional
    public boolean releaseLock(UUID cartId) {
        return cartRepository.releaseLock(cartId, AuditorAwareUtil.getAuditor()) == UPDATE_SUCCESS;
    }
}

