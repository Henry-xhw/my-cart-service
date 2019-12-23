package com.active.services.cart.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.BaseTree;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFeeRelationship;
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

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    private final CartItemFeeRepository cartItemFeeRepository;

    private final CartPriceEngine cartPriceEngine;

    private final DataAccess dataAccess;

    private static final int UPDATE_SUCCESS = 1;

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
                " cart id does not exist: " + cartId));
        TreeBuilder<CartItem> treeBuilder = new TreeBuilder<>(cart.getItems());
        cart.setItems(treeBuilder.buildTree());
        return cart;
    }

    @Transactional
    public List<CartItem> createCartItems(Long cartId, UUID cartIdentifier, List<CartItem> items) {
        cartRepository.createCartItems(cartId, items);
        if (!incrementVersion(cartIdentifier)){
            throw new CartException(ErrorCode.INTERNAL_ERROR, "increment version fail");
        }
        return items;
    }

    @Transactional
    public long createCartItem(Long cartId, UUID cartIdentifier, BaseTree<CartItem> cartItem) {
        long result = cartRepository.createCartItem(cartId, cartItem);
        if (!incrementVersion(cartIdentifier)){
            throw new CartException(ErrorCode.INTERNAL_ERROR, "increment version fail");
        }
        return result;
    }

    @Transactional
    public List<CartItem> updateCartItems(UUID cartIdentifier, List<CartItem> items) {
        Cart cart = getCartByUuid(cartIdentifier);
        items.forEach(cartItem -> {
            if (!cart.findCartItem(cartItem.getIdentifier()).isPresent()) {
                throw new CartException(ErrorCode.VALIDATION_ERROR, " cart item id: "
                        + cartItem.getIdentifier() + " is not belong cart id:" + cart.getIdentifier());
            }
        });
        cartRepository.updateCartItems(items);
        if (!incrementVersion(cartIdentifier)){
            throw new CartException(ErrorCode.INTERNAL_ERROR, "increment version fail");
        }
        return items;
    }

    @Transactional
    public void deleteCartItem(Cart cart, UUID cartItemUuid) {
        Optional<CartItem> cartItem = cart.findCartItem(cartItemUuid);
        if (!cartItem.isPresent()) {
            throw new CartException(ErrorCode.VALIDATION_ERROR, " cart item id: "
                    + cartItemUuid + " is not belong cart id:" + cart.getIdentifier());
        }
        List<UUID> idsToDelete = cartItem.get().getFlattenSubItems()
                        .stream()
                        .map(CartItem::getIdentifier).collect(Collectors.toList());

        cartRepository.batchDeleteCartItems(idsToDelete);
        if (!incrementVersion(cart.getIdentifier())){
            throw new CartException(ErrorCode.INTERNAL_ERROR, "increment version fail");
        }
    }

    public List<UUID> search(UUID ownerId) {
        return cartRepository.search(ownerId);
    }

    public Long getCartItemIdByCartItemUuid(UUID cartItemId) {
        return cartRepository.getCartItemIdByCartItemUuid(cartItemId)
                .orElseThrow(() -> new CartException(ErrorCode.CART_ITEM_NOT_FOUND, " cartItem id: " + cartItemId));
    }

    public void insertCartItems(Cart cart, List<CartItem> cartItemList, Long requestParentId) {
        Long cartId = cart.getId();
        for (CartItem cartItem : cartItemList) {
            Long parentId = requestParentId;
            if (cartItem.getIdentifier() != null) {
                if (!cart.findCartItem(cartItem.getIdentifier()).isPresent()) {
                    // cart item not exist, need error msg
                    throw new CartException(ErrorCode.VALIDATION_ERROR, " cart item id: "
                            + cartItem.getIdentifier() + " is not belong cart id:" + cart.getIdentifier());
                }
                parentId = getCartItemIdByCartItemUuid(cartItem.getIdentifier());
            } else {
                cartItem.setIdentifier(UUID.randomUUID());
                cartItem.setParentId(parentId);
                parentId = createCartItem(cartId, cart.getIdentifier(), cartItem);
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
