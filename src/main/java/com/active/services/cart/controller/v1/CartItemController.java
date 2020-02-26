package com.active.services.cart.controller.v1;

import com.active.services.cart.controller.v1.mapper.CartMapper;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.v1.req.CreateCartItemReq;
import com.active.services.cart.model.v1.req.UpdateCartItemReq;
import com.active.services.cart.service.CartService;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.active.services.cart.controller.Constants.CART_ID_PARAM;
import static com.active.services.cart.controller.Constants.CART_ITEM_ID_PARAM;
import static com.active.services.cart.controller.Constants.CART_ITEM_ID_PATH;
import static com.active.services.cart.controller.Constants.V1_MEDIA;

@RestController
@RequestMapping(value = "/carts/{cart-id}/items", consumes = V1_MEDIA, produces = V1_MEDIA)
@RequiredArgsConstructor
public class CartItemController {

    private final CartService cartService;

    @PostMapping
    public void create(@PathVariable(CART_ID_PARAM) UUID cartIdentifier,
                                    @RequestBody @Validated CreateCartItemReq req) {
        List<CartItem> items = req.getItems()
                .stream()
                .map(item -> CartMapper.INSTANCE.toDomain(item, true))
                .collect(Collectors.toList());
        cartService.insertCartItems(cartIdentifier, items);
    }

    @PutMapping
    public void update(@PathVariable(CART_ID_PARAM) UUID cartIdentifier,
                                    @RequestBody @Validated UpdateCartItemReq req) {
        List<CartItem> items = CartMapper.INSTANCE.toDomain(req.getItems());
        cartService.updateCartItems(cartIdentifier, items);
    }

    @DeleteMapping(CART_ITEM_ID_PATH)
    public void delete(@PathVariable(CART_ID_PARAM) UUID cartId,
                                    @PathVariable(CART_ITEM_ID_PARAM) UUID cartItemId) {
        Cart cart = cartService.getCartByUuid(cartId);
        cartService.deleteCartItem(cart, cartItemId);
    }
}