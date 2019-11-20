package com.active.services.cart.controller.v1;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.v1.req.CreateCartItemReq;
import com.active.services.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.active.services.cart.controller.v1.Constants.*;

@RestController
@RequestMapping(value = "/carts", consumes = V1_MEDIA, produces = V1_MEDIA)
public class CartItemController {

    private static final String CART_ID_PARAM = "cart-id";
    private static final String CART_ID_PATH = "/{" + CART_ID_PARAM + "}/items";

    @Autowired
    private CartService cartService;

    @PostMapping(CART_ID_PATH)
    public CreateCartItemReq create(@PathVariable(CART_ID_PARAM) UUID cartId,
                                    @RequestBody CreateCartItemReq req) {
        return upsert(cartId, req, true);
    }

    @PutMapping(CART_ID_PATH)
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

    @DeleteMapping(CART_ID_PATH)
    public void delete(@PathVariable(CART_ID_PARAM) UUID cartItemId) {
        cartService.deleteCartItem(cartItemId);
    }
}
