package com.active.services.cart.controller.v1;

import com.active.services.cart.common.OperationResultCode;
import com.active.services.cart.common.exception.CartException;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.Range;
import com.active.services.cart.model.v1.req.CreateCartItemReq;
import com.active.services.cart.model.v1.req.UpdateCartItemReq;
import com.active.services.cart.model.v1.rsp.CreateCartItemRsp;
import com.active.services.cart.model.v1.rsp.DeleteCartItemRsp;
import com.active.services.cart.model.v1.rsp.UpdateCartItemRsp;
import com.active.services.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.active.services.cart.controller.v1.Constants.V1_MEDIA;

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
                .peek(cartItemDto -> checkTimeRange(cartItemDto.getBookingRange(), cartItemDto.getTrimmedBookingRange()))
                .map(item -> CartMapper.INSTANCE.toDomain(item, true))
                .collect(Collectors.toList());

        cartService.createCartItems(cartId, items);
        CreateCartItemRsp rsp = new CreateCartItemRsp();
        rsp.setCartId(cartIdentifier);
        return rsp;
    }

    @PutMapping()
    public UpdateCartItemRsp update(@PathVariable(CART_ID_PARAM) UUID cartIdentifier,
                                    @RequestBody @Validated UpdateCartItemReq req) {
        Cart cart = cartService.get(cartIdentifier);
        req.getItems().stream().forEach(cartItem -> {
            checkTimeRange(cartItem.getBookingRange(), cartItem.getTrimmedBookingRange());
            if(!isCartItemExist(cart.getItems(), cartItem.getIdentifier())) {
                throw new CartException(OperationResultCode.CART_ITEM_NOT_EXIST.getCode(),
                        String.format("cart item not exist: %s", cartItem.getIdentifier().toString()));
            }
        });
        List<CartItem> items = req.getItems().stream().map(CartItem::new).collect(Collectors.toList());
        UpdateCartItemRsp rsp = new UpdateCartItemRsp();
        cartService.updateCartItems(items);
        rsp.setCartId(cartIdentifier);

        return rsp;
    }

    @DeleteMapping(CART_ITEM_ID_PATH)
    public DeleteCartItemRsp delete(@PathVariable("cart-id") UUID cartId,
                                    @PathVariable(CART_ITEM_ID_PARAM) UUID cartItemId) {
        if (!isCartItemExist(cartService.get(cartId).getItems(), cartItemId)) {
            // cart item not exist, need error msg
            throw new CartException(OperationResultCode.CART_ITEM_NOT_EXIST.getCode(),
                    OperationResultCode.CART_ITEM_NOT_EXIST.getDescription()
                            + " cart id: " + cartId
                            + " cart item id: " + cartItemId);
        }

        cartService.deleteCartItem(cartItemId);
        DeleteCartItemRsp rsp = new DeleteCartItemRsp();
        rsp.setCartId(cartId);
        return rsp;
    }

    private boolean isCartItemExist(List<CartItem> items, UUID cartItemId) {
        return items.stream().anyMatch(it -> it.getIdentifier().toString().equals(cartItemId.toString()));
    }

    private void checkTimeRange(Range<Instant> bookingRange, Range<Instant> trimmedBookingRange) {
        if (Objects.nonNull(bookingRange) && !bookingRange.valid()) {
            throw new CartException(OperationResultCode.INVALID_PARAMETER.getCode(),
                    OperationResultCode.INVALID_PARAMETER.getDescription() + " booking range");
        }
        if (Objects.nonNull(trimmedBookingRange) && !trimmedBookingRange.valid()) {
            throw new CartException(OperationResultCode.INVALID_PARAMETER.getCode(),
                    OperationResultCode.INVALID_PARAMETER.getDescription() + " trimmed booking range");
        }
    }
}