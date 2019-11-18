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
@RequestMapping(value = "/carts/{cart-id}/items", consumes = V1_MEDIA, produces = V1_MEDIA)
public class CartItemController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public CreateCartItemReq create(@RequestParam("cart-id") UUID cartId,
                                    @RequestBody CreateCartItemReq req) {
        return upsert(cartId, req, true);
    }

    @PutMapping
    public CreateCartItemReq update(@RequestParam("cart-id") UUID cartId,
                                    @RequestBody CreateCartItemReq req) {
        return upsert(cartId, req, false);
    }

    private CreateCartItemReq upsert(UUID cartId, CreateCartItemReq req, boolean isCreate) {
        CreateCartItemReq rsp = new CreateCartItemReq();

        List<CartItem> items = req.getItems().stream().map(item ->
                CartMapper.INSTANCE.toDomain(item, isCreate)).collect(Collectors.toList());
        items = cartService.upsertItems(cartId, items);
        rsp.setItems(items.stream().map(item -> CartMapper.INSTANCE.toDto(item)).collect(Collectors.toList()));

        return rsp;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID cartItemId) {
        cartService.deleteCartItem(cartItemId);
    }
}
