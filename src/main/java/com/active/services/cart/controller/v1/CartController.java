package com.active.services.cart.controller.v1;

import com.active.services.cart.domain.Cart;
import com.active.services.cart.model.v1.CartDto;
import com.active.services.cart.model.v1.req.CreateCartReq;
import com.active.services.cart.model.v1.req.SearchCartReq;
import com.active.services.cart.model.v1.rsp.SearchCartRsp;
import com.active.services.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.active.services.cart.controller.v1.CartController.V1_MEDIA;

@RestController
@RequestMapping(value = "/cart", consumes = V1_MEDIA, produces = V1_MEDIA)
public class CartController {

    public static final String V1_MEDIA = "application/vnd.active.cart-service.v1+json";

    @Autowired
    private CartService cartService;

    @PutMapping
    public CreateCartReq createCart(CreateCartReq req) {
        CreateCartReq rsp = new CreateCartReq();

        CartDto cartDto = req.getCart();
        cartDto.setItems(new ArrayList<>());
        Cart cart = CartMapper.INSTANCE.toDomain(cartDto, true);
        cartService.create(cart);
        rsp.setCart(CartMapper.INSTANCE.toDto(cart));

        return rsp;
    }

    @DeleteMapping("{id}")
    public void deleteCart(@PathVariable UUID cartId) {
        cartService.delete(cartId);
    }

    @GetMapping("{id")
    public CreateCartReq get(@PathVariable UUID cartId) {
        CreateCartReq rsp = new CreateCartReq();

        Cart cart = cartService.get(cartId);
        rsp.setCart(CartMapper.INSTANCE.toDto(cart));

        return rsp;
    }

    @PostMapping
    public SearchCartRsp searchCart(SearchCartReq req) {
        SearchCartRsp rsp = new SearchCartRsp();

        List<UUID> cartIds = cartService.search(req.getOwnerId());
        rsp.setCartIds(cartIds);

        return rsp;
    }
}
