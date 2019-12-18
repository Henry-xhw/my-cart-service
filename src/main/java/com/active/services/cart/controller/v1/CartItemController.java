package com.active.services.cart.controller.v1;

import static com.active.services.cart.controller.v1.Constants.V1_MEDIA;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.v1.req.CreateCartItemReq;
import com.active.services.cart.model.v1.req.UpdateCartItemReq;
import com.active.services.cart.model.v1.rsp.CreateCartItemRsp;
import com.active.services.cart.model.v1.rsp.DeleteCartItemRsp;
import com.active.services.cart.model.v1.rsp.UpdateCartItemRsp;
import com.active.services.cart.service.CartService;

@RestController
@RequestMapping(value = "/carts/{cart-id}/items", consumes = V1_MEDIA, produces = V1_MEDIA)
public class CartItemController {
    private static final String CART_ID_PARAM = "cart-id";
    private static final String CART_ITEM_ID_PARAM = "cart-item-id";
    private static final String CART_ITEM_ID_PATH = "/{" + CART_ITEM_ID_PARAM + "}";

    @Autowired
    private CartService cartService;

    @PostMapping()
    public CreateCartItemRsp create(@PathVariable(CART_ID_PARAM) UUID cartIdentifier,
                                    @RequestBody @Validated CreateCartItemReq req) {

        Long cartId = cartService.get(cartIdentifier).getId();

        List<CartItem> items = req.getItems()
                .stream()
                .map(item -> CartMapper.INSTANCE.toDomain(item, true))
                .collect(Collectors.toList());

        cartService.createCartItems(cartId, cartIdentifier, items);
        CreateCartItemRsp rsp = new CreateCartItemRsp();
        rsp.setCartId(cartIdentifier);
        return rsp;
    }

    @PutMapping()
    public UpdateCartItemRsp update(@PathVariable(CART_ID_PARAM) UUID cartIdentifier,
                                    @RequestBody @Validated UpdateCartItemReq req) {
        List<CartItem> items = req.getItems().stream().map(CartItem::new).collect(Collectors.toList());
        UpdateCartItemRsp rsp = new UpdateCartItemRsp();
        cartService.updateCartItems(cartIdentifier, items);
        rsp.setCartId(cartIdentifier);

        return rsp;
    }

    @DeleteMapping(CART_ITEM_ID_PATH)
    public DeleteCartItemRsp delete(@PathVariable("cart-id") UUID cartId,
                                    @PathVariable(CART_ITEM_ID_PARAM) UUID cartItemId) {
        cartService.deleteCartItem(cartId, cartItemId);
        DeleteCartItemRsp rsp = new DeleteCartItemRsp();
        rsp.setCartId(cartId);
        return rsp;
    }

    private boolean isCartItemExist(List<CartItem> items, UUID cartItemId) {
        return items.stream().anyMatch(it -> it.getIdentifier().toString().equals(cartItemId.toString()));
    }
}