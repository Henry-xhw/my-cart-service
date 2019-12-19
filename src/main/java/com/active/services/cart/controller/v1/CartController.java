package com.active.services.cart.controller.v1;

import static com.active.services.cart.controller.v1.Constants.ID_PARAM;
import static com.active.services.cart.controller.v1.Constants.ID_PARAM_PATH;
import static com.active.services.cart.controller.v1.Constants.V1_MEDIA;

import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.model.v1.req.CheckoutReq;
import com.active.services.cart.model.v1.req.CreateCartReq;
import com.active.services.cart.model.v1.rsp.CheckoutRsp;
import com.active.services.cart.model.v1.CartDto;
import com.active.services.cart.model.v1.rsp.CreateCartRsp;
import com.active.services.cart.model.v1.rsp.FindCartByIdRsp;
import com.active.services.cart.model.v1.rsp.SearchCartRsp;
import com.active.services.cart.service.CartService;

@RestController
@RequestMapping(value = "/carts", consumes = V1_MEDIA, produces = V1_MEDIA)
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public CreateCartRsp create(@RequestBody @NotNull @Validated CreateCartReq req) {
        Cart cart = CartMapper.INSTANCE.toDomainFromCreateCartReq(req, true);
        cartService.create(cart);
        CreateCartRsp rsp = new CreateCartRsp();
        CartDto cartDto = CartMapper.INSTANCE.toDto(cart);
        rsp.setCart(cartDto);

        return rsp;
    }

    @DeleteMapping(ID_PARAM_PATH)
    public void delete(@PathVariable(ID_PARAM) UUID cartIdentifier) {
        cartService.delete(cartService.get(cartIdentifier).getId());
    }

    @GetMapping(ID_PARAM_PATH)
    public FindCartByIdRsp get(@PathVariable(ID_PARAM) UUID cartId) {
        FindCartByIdRsp rsp = new FindCartByIdRsp();
        rsp.setCart(CartMapper.INSTANCE.toDto(cartService.get(cartId)));
        return rsp;
    }

    @GetMapping(value = "/ownerId/{ownerId}")
    public SearchCartRsp search(@PathVariable("ownerId") UUID ownerId) {
        SearchCartRsp rsp = new SearchCartRsp();

        List<UUID> cartIds = cartService.search(ownerId);
        rsp.setCartIds(cartIds);

        return rsp;
    }

    @PostMapping("/{cartId}/checkout")
    public CheckoutRsp checkout(@PathVariable UUID cartId, @NotNull @RequestBody @Validated CheckoutReq req) {
        CheckoutRsp rsp = new CheckoutRsp();
        return rsp;
    }
}
