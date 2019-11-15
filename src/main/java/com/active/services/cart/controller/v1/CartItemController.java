package com.active.services.cart.controller.v1;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.v1.req.CreateCartItemReq;
import com.active.services.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.active.services.cart.controller.v1.CartController.V1_MEDIA;

@RestController
@RequestMapping(value = "/cart-item", consumes = V1_MEDIA, produces = V1_MEDIA)
public class CartItemController {

    @Autowired
    private CartService cartService;

    /**
     * Create item if no uuid/else update it.
     *
     * @param req
     * @return
     */
    @PutMapping
    public CreateCartItemReq create(CreateCartItemReq req) {
        CreateCartItemReq rsp = new CreateCartItemReq();

        List<CartItem> items = req.getItems().stream().map(item ->
                CartMapper.INSTANCE.toDomain(item, false)).collect(Collectors.toList());
        items = cartService.upsertItems(req.getCartId(), items);
        rsp.setItems(items.stream().map(item -> CartMapper.INSTANCE.toDto(item)).collect(Collectors.toList()));

        return rsp;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable UUID cartItemId) {
        cartService.deleteCartItem(cartItemId);
    }
}
