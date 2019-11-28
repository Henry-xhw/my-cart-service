package com.active.services.cart.controller.v1;

import com.active.services.cart.common.OperationResultCode;
import com.active.services.cart.common.exception.CartException;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.model.v1.CartDto;
import com.active.services.cart.model.v1.req.CreateCartReq;
import com.active.services.cart.model.v1.rsp.FindCartByIdRsp;
import com.active.services.cart.model.v1.rsp.SearchCartRsp;
import com.active.services.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.active.services.cart.controller.v1.Constants.*;

@RestController
@RequestMapping(value = "/carts", consumes = V1_MEDIA, produces = V1_MEDIA)
public class CartController {

    private static final String CART_NOT_EXIST = "cart `%s` not exist";

    @Autowired
    private CartService cartService;

    @PostMapping
    public CreateCartReq create(@RequestBody @Validated CreateCartReq req) {
        CreateCartReq rsp = new CreateCartReq();

        CartDto cartDto = req.getCart();
        cartDto.setItems(new ArrayList<>());
        Cart cart = CartMapper.INSTANCE.toDomain(cartDto, true);
        cartService.create(cart);
        rsp.setCart(CartMapper.INSTANCE.toDto(cart));

        return rsp;
    }

    @DeleteMapping(ID_PARAM_PATH)
    public void delete(@PathVariable(ID_PARAM) UUID cartIdentifier) {
        Long cartId = Optional.ofNullable(cartService.get(cartIdentifier))
          .map(Cart::getId)
          .orElseThrow(() -> new CartException(OperationResultCode.CART_NOT_EXIST.getCode(),
            OperationResultCode.CART_NOT_EXIST.getDescription() + " cart id: " + cartIdentifier.toString()));
        cartService.delete(cartId);
    }

    @GetMapping(ID_PARAM_PATH)
    public FindCartByIdRsp get(@PathVariable(ID_PARAM) UUID cartId) {
        FindCartByIdRsp rsp = new FindCartByIdRsp();

        Cart cart = cartService.get(cartId);
        rsp.setCart(CartMapper.INSTANCE.toDto(cart));

        return rsp;
    }

    @GetMapping(value = "/ownerId/{ownerId}")
    public SearchCartRsp search(@PathVariable("ownerId") UUID ownerId) {
        SearchCartRsp rsp = new SearchCartRsp();

        List<UUID> cartIds = cartService.search(ownerId);
        rsp.setCartIds(cartIds);

        return rsp;
    }
}
