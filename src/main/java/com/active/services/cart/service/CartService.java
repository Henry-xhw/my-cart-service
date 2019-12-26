package com.active.services.cart.service;

import com.active.services.cart.client.rest.OrderService;
import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.BaseTree;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.domain.CartItemFeeRelationship;
import com.active.services.cart.domain.CartItemFeesInCart;
import com.active.services.cart.infrastructure.mapper.PlaceCartMapper;
import com.active.services.cart.model.ErrorCode;
import com.active.services.cart.model.PaymentAccount;
import com.active.services.cart.model.PaymentAccountResult;
import com.active.services.cart.model.v1.CheckoutResult;
import com.active.services.cart.model.v1.req.CheckoutReq;
import com.active.services.cart.repository.CartItemFeeRepository;
import com.active.services.cart.repository.CartRepository;
import com.active.services.cart.service.quote.CartPriceEngine;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.util.AuditorAwareUtil;
import com.active.services.cart.util.DataAccess;
import com.active.services.cart.util.TreeBuilder;
import com.active.services.order.management.api.v3.types.OrderDTO;
import com.active.services.order.management.api.v3.types.OrderResponseDTO;
import com.active.services.order.management.api.v3.types.PlaceOrderReq;
import com.active.services.order.management.api.v3.types.PlaceOrderRsp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {
    private final CartRepository cartRepository;
    private final OrderService orderService;

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
        return cart;
    }

    public Cart getCartWithFullPriceByUuid(UUID cartId) {
        Cart cart = getCartByUuid(cartId);
        buildCartItemFeeTree(cart);
        return cart;
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
        incrementVersion(cart.getIdentifier());
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
            incrementPriceVersion(cartId);
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

    @Transactional
    public List<CheckoutResult> checkout(UUID cartId, CheckoutReq req) {
        Cart cart = getCartWithFullPriceByUuid(cartId);

        if (CollectionUtils.isEmpty(cart.getFlattenCartItems())) {
            throw new CartException(ErrorCode.CART_ITEM_NOT_FOUND,
                    "There is no cart item for cartId: {0} ", cartId);
        }
        if (cart.getVersion() != cart.getPriceVersion()) {
            throw new CartException(ErrorCode.CART_PRICING_OUT_OF_DATE,
                    "Cart: {0} price had out of date. Price version : {1}, cart version: {2}. Please call quote " +
                            "before checkout.",
                    cartId, cart.getPriceVersion(), cart.getVersion());
        }
        if (!acquireLock(cartId)) {
            LOG.warn("Cart {} had been locked by other call", cartId);
            throw new CartException(ErrorCode.CART_LOCKED, "Cart: {0} had been locked by other call.", cartId);
        }

        PlaceOrderRsp rsp;
        try {
            commitInventory(cart.getItems().stream().map(CartItem::getIdentifier).collect(Collectors.toList()));
            commitPayment();
            rsp = placeOrder(cart, req.getOrderUrl(), req.isSendReceipt(),
                    Optional.ofNullable(req.getPaymentAccount()).map(PaymentAccount::getAmsAccountId).orElse(null));
        } catch (CartException e) {
            if (ErrorCode.PAYMENT_AUTH_ERROR == e.getErrorCode()) {
                // reverse inventory.
            }
            LOG.info(e.getErrorMessage());
            throw e;
        } finally {
            releaseLock(cartId);
        }
        finalizeCart(cartId);
        return rsp.getOrderResponses().stream().map(OrderResponseDTO::getOrderId)
                .map(orderId -> new CheckoutResult(orderId, new PaymentAccountResult()))
                .collect(Collectors.toList());
    }

    private PlaceOrderRsp placeOrder(Cart cart, String orderUrl, boolean sendReceipt, String payAccountId) {
        OrderDTO orderDTO = PlaceCartMapper.MAPPER.toOrderDTO(cart);
        orderDTO.setOrderUrl(orderUrl);
        orderDTO.setSendOrderReceipt(sendReceipt);
        orderDTO.setPayAccountId(payAccountId);
        PlaceOrderReq req = new PlaceOrderReq();
        req.setOrderDTO(orderDTO);
        PlaceOrderRsp rsp = null;
        try {
            rsp = orderService.placeOrder(req);
        } catch (Exception e) {
            handleException(e);
        }
        if (rsp == null || CollectionUtils.isEmpty(rsp.getOrderResponses())) {
            throw new CartException(ErrorCode.PLACE_ORDER_ERROR, "There is no order response from order service for " +
                    "cart: {0}",
                    cart.getIdentifier());
        }
        return rsp;
    }

    private void handleException(Exception e) {
        // do we need to update cart status?
    }

    private void commitInventory(List<UUID> inventoryReservationIds) {
    }

    private void commitPayment() {
    }
}