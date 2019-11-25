package com.active.services.cart.controller.v1;

import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.v1.req.CreateCartItemReq;
import com.active.services.cart.model.v1.rsp.DeleteCartItemRsp;
import com.active.services.cart.service.CartService;
import com.active.services.common.exception.CartItemException;
import org.eclipse.jetty.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.active.services.cart.controller.v1.Constants.*;

@RestController
@RequestMapping(value = "/carts/{cart-id}/items", consumes = V1_MEDIA, produces = V1_MEDIA)
public class CartItemController {
    private static final String CART_ID_PARAM = "cart-id";
    private static final String CART_ITEM_ID_PARAM = "cart-item-id";
    private static final String CART_ITEM_ID_PATH = "/{" + CART_ITEM_ID_PARAM + "}";

    @Autowired
    private CartService cartService;

    @PostMapping()
    public CreateCartItemReq create(@PathVariable(CART_ID_PARAM) UUID cartId,
                                    @RequestBody CreateCartItemReq req) {
        return upsert(cartId, req, true);
    }

    @PutMapping()
    public CreateCartItemReq update(@PathVariable(CART_ID_PARAM) UUID cartId,
                                    @RequestBody CreateCartItemReq req) {
        return upsert(cartId, req, false);
    }

    private CreateCartItemReq upsert(UUID cartId, CreateCartItemReq req, boolean isCreate) {
        CreateCartItemReq rsp = new CreateCartItemReq();

        List<CartItem> items = req.getItems().stream().map(item ->
                CartMapper.INSTANCE.toDomain(item, isCreate)).collect(Collectors.toList());
        items = cartService.upsertItems(cartId, items);
        rsp.setItems(items.stream().map(CartMapper.INSTANCE::toDto).collect(Collectors.toList()));

        return rsp;
    }

    @DeleteMapping(CART_ITEM_ID_PATH)
    public DeleteCartItemRsp delete(@PathVariable("cart-id") UUID cartId,
                                    @PathVariable(CART_ITEM_ID_PARAM) UUID cartItemId) {
        Cart cart = cartService.get(cartId);
        if (null == cart) {
            // cart not exist, need error msg
            throw new CartItemException(HttpStatus.NOT_FOUND_404, "cart not exist", cartId);
        }

        if (cart.getItems().size() == 0) {
            // empty cart, need error msg
            throw new CartItemException(HttpStatus.NOT_FOUND_404, "cart item not exist", cartId);
        }

        if (!isCartItemExist(cart.getItems(), cartItemId)) {
            // cart item not exist, need error msg
            throw new CartItemException(HttpStatus.NOT_FOUND_404, "cart item not exist", cartId);
        }

        cartService.deleteCartItem(cartItemId);
        DeleteCartItemRsp rsp = new DeleteCartItemRsp();
        rsp.setCartId(cartId);
        return rsp;
    }

    private boolean isCartItemExist(List<CartItem> items, UUID cartItemId) {
        return items.stream().filter(it -> it.getIdentifier() == cartItemId).count() != 0;
    }
}
