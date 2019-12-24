package com.active.services.cart.service;

import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.BaseTree;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.domain.CartItemFeeRelationship;
import com.active.services.cart.domain.CartItemFeesInCart;
import com.active.services.cart.model.ErrorCode;
import com.active.services.cart.repository.CartItemFeeRepository;
import com.active.services.cart.repository.CartRepository;
import com.active.services.cart.service.quote.CartPriceEngine;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.util.AuditorAwareUtil;
import com.active.services.cart.util.DataAccess;
import com.active.services.cart.util.TreeBuilder;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    private final CartItemFeeRepository cartItemFeeRepository;

    private final CartPriceEngine cartPriceEngine;

    private final DataAccess dataAccess;

    @Transactional
    public void create(Cart cart) {
        cartRepository.createCart(cart);
    }

    @Transactional
    public void delete(Long cartId) {
        cartRepository.deleteCart(cartId);
    }

    public Cart getCartByUuid(UUID cartId) {
        Cart cart = cartRepository.getCart(cartId).orElseThrow(() -> new CartException(ErrorCode.CART_NOT_FOUND,
                " cart id does not exist: {0}", cartId));
        buildCartItemTree(cart);
        return cart;
    }

    public Cart loadCartByUuid(UUID cartId) {
        Cart cart = getCartByUuid(cartId);
        buildCartItemFeeTree(cart);
        return cart;
    }

    private void buildCartItemTree(Cart cart) {
        TreeBuilder<CartItem> treeBuilder = new TreeBuilder<>(cart.getItems());
        cart.setItems(treeBuilder.buildTree());
    }

    private void buildCartItemFeeTree(Cart cart) {
        List<CartItemFeesInCart> cartItemFees = cartItemFeeRepository.getCartItemFeesByCartId(cart.getId());
        cart.getItems().forEach(cartItem -> {
            List<CartItemFeesInCart> collect =
                    cartItemFees.stream().filter(itemFee -> itemFee.getCartItemId() == cartItem.getId())
                            .collect(Collectors.toList());

            TreeBuilder<CartItemFee> baseTreeTreeBuilder = new TreeBuilder<>(collect);
            cartItem.setFees(baseTreeTreeBuilder.buildTree());
        });
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
        Cart cart = getCartByUuid(cartIdentifier);
        items.forEach(it -> cart.findCartItem(it.getIdentifier())
                .orElseThrow(() -> new CartException(ErrorCode.VALIDATION_ERROR,
                "cart item id: {0} is not belong cart id: {1}", it.getIdentifier(), cart.getIdentifier())));
        cartRepository.updateCartItems(items);
        incrementVersion(cartIdentifier);
        return items;
    }

    @Transactional
    public void deleteCartItem(Cart cart, UUID cartItemUuid) {
        CartItem cartItem = cart.findCartItem(cartItemUuid)
                .orElseThrow(() -> new CartException(ErrorCode.VALIDATION_ERROR,
                        "cart item id: {0} is not belong cart id: {1}", cartItemUuid, cart.getIdentifier()));
        List<UUID> idsToDelete = cartItem.getFlattenSubItems()
                        .stream()
                        .map(CartItem::getIdentifier).collect(Collectors.toList());

        cartRepository.batchDeleteCartItems(idsToDelete);
        incrementVersion(cart.getIdentifier());
    }

    public List<UUID> search(UUID ownerId) {
        return cartRepository.search(ownerId);
    }

    public Long getCartItemIdByCartItemUuid(UUID cartItemId) {
        return cartRepository.getCartItemIdByCartItemUuid(cartItemId)
                .orElseThrow(() -> new CartException(ErrorCode.CART_ITEM_NOT_FOUND, " cartItem id: {0}", cartItemId));
    }

    public void insertCartItems(Cart cart, List<CartItem> cartItemList, Long requestParentId) {
        Long cartId = cart.getId();
        for (CartItem it : cartItemList) {
            Long parentId = requestParentId;
            if (it.getIdentifier() != null) {
                cart.findCartItem(it.getIdentifier())
                        .orElseThrow(() -> new CartException(ErrorCode.VALIDATION_ERROR,
                                "cart item id: {0} is not belong cart id: {1}", it.getIdentifier(), cart.getIdentifier()));
                parentId = getCartItemIdByCartItemUuid(it.getIdentifier());
            } else {
                it.setIdentifier(UUID.randomUUID());
                it.setParentId(parentId);
                parentId = createCartItem(cartId, it);
            }
            List<CartItem> subItems = it.getSubItems();
            if (subItems.size() > 0) {
                insertCartItems(cart, subItems, parentId);
            }
        }
    }

    @Transactional
    public void finalizeCart(UUID cartId) {
        cartRepository.finalizeCart(cartId, AuditorAwareUtil.getAuditor());
    }

    @Transactional
    public void incrementVersion(UUID cartId) {
        cartRepository.incrementVersion(cartId, AuditorAwareUtil.getAuditor());
    }

    @Transactional
    public void incrementPriceVersion(UUID cartId) {
        cartRepository.incrementPriceVersion(cartId, AuditorAwareUtil.getAuditor());
    }

    @Transactional
    public boolean acquireLock(UUID cartId) {
        return cartRepository.acquireLock(cartId, AuditorAwareUtil.getAuditor()) == 1;
    }

    @Transactional
    public boolean releaseLock(UUID cartId) {
        return cartRepository.releaseLock(cartId, AuditorAwareUtil.getAuditor()) == 1;
    }

    public Cart quote(UUID cartId) {
        Cart cart = getCartByUuid(cartId);
        cartPriceEngine.quote(new CartQuoteContext(cart));

        // Manual control the tx
        dataAccess.doInTx(() -> {
            saveQuoteResult(cart);
        });
        return cart;
    }

    private void saveQuoteResult(Cart cart) {
        cart.getItems().stream().filter(Objects::nonNull).forEach(item -> {
            cartItemFeeRepository.deleteLastQuoteResult(item.getId());
            item.getFees().stream().filter(Objects::nonNull).forEach(cartItemFee -> {
                cartItemFeeRepository.createCartItemFee(cartItemFee);
                cartItemFeeRepository.createCartItemCartItemFee(
                    CartItemFeeRelationship.buildCartItemCartItemFee(item.getId(), cartItemFee.getId()));
            });
        });
    }

}
