package com.active.services.cart.controller.v1;

import com.active.services.cart.common.exception.CartException;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.v1.req.CreateCartItemReq;
import com.active.services.cart.model.v1.rsp.CreateCartItemRsp;
import com.active.services.cart.model.v1.rsp.DeleteCartItemRsp;
import com.active.services.cart.service.CartService;
import org.eclipse.jetty.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.active.services.cart.controller.v1.Constants.*;

@RestController
@RequestMapping(value = "/carts/{cart-id}/items", consumes = V1_MEDIA, produces = V1_MEDIA)
public class CartItemController {
    private static final String CART_ID_PARAM = "cart-id";
    private static final String CART_ITEM_ID_PARAM = "cart-item-id";
    private static final String CART_ITEM_ID_PATH = "/{" + CART_ITEM_ID_PARAM + "}";

    private static final String CART_NOT_EXIST = "cart `%s` not exist";

    @Autowired
    private CartService cartService;

    @PostMapping()
    public CreateCartItemRsp create(@PathVariable(CART_ID_PARAM) UUID cartIdentifier,
                                    @RequestBody @Validated CreateCartItemReq req) {
        Long cartId = Optional.ofNullable(cartService.get(cartIdentifier))
          .map(Cart::getId)
          .orElseThrow(() -> new CartException(HttpStatus.NOT_FOUND_404, String.format(CART_NOT_EXIST, cartIdentifier)));

        List<CartItem> items = req.getItems()
          .stream()
          .peek(item -> {
              if (Objects.nonNull(item.getBookingRange()) && !item.getBookingRange().valid()) {
                  throw new CartException(HttpStatus.BAD_REQUEST_400, String.format("booking range invalid"));
              }
              if (Objects.nonNull(item.getTrimmedBookingRange()) && !item.getTrimmedBookingRange().valid()) {
                  throw new CartException(HttpStatus.BAD_REQUEST_400, String.format("trimmed booking range invalid"));
              }
          })
          .map(item -> CartMapper.INSTANCE.toDomain(item, true))
          .collect(Collectors.toList());

        cartService.createCartItems(cartId, items);
        CreateCartItemRsp rsp = new CreateCartItemRsp();
        rsp.setCartId(cartIdentifier);
        return rsp;
    }

    @PutMapping()
    public CreateCartItemReq update(@PathVariable(CART_ID_PARAM) UUID cartId,
                                    @RequestBody CreateCartItemReq req) {
        CreateCartItemReq rsp = new CreateCartItemReq();

        List<CartItem> items = req.getItems().stream().map(item ->
          CartMapper.INSTANCE.toDomain(item, false)).collect(Collectors.toList());
        items = cartService.updateCartItems(items);
        rsp.setItems(items.stream().map(CartMapper.INSTANCE::toDto).collect(Collectors.toList()));

        return rsp;
    }

    @DeleteMapping(CART_ITEM_ID_PATH)
    public DeleteCartItemRsp delete(@PathVariable("cart-id") UUID cartId,
                                    @PathVariable(CART_ITEM_ID_PARAM) UUID cartItemId) {
        Cart cart = cartService.get(cartId);
        if (null == cart) {
            // cart not exist, need error msg
            throw new CartException(HttpStatus.NOT_FOUND_404, String.format("cart item not exist: %s", cartId.toString()));
        }

        if (cart.getItems().size() == 0) {
            // empty cart, need error msg
            throw new CartException(HttpStatus.NOT_FOUND_404, String.format("cart item not exist: %s", cartId.toString()));
        }

        if (!isCartItemExist(cart.getItems(), cartItemId)) {
            // cart item not exist, need error msg
            throw new CartException(HttpStatus.NOT_FOUND_404, String.format("cart item not exist: %s", cartId.toString()));
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
