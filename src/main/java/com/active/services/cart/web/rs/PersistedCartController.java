package com.active.services.cart.web.rs;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.active.services.cart.application.CartService;
import com.active.services.cart.model.AddItemToCartReq;
import com.active.services.cart.model.AddItemToCartResp;
import com.active.services.cart.model.ApplyDiscountToCartReq;
import com.active.services.cart.model.ApplyDiscountToCartResp;
import com.active.services.cart.model.CreateCartReq;
import com.active.services.cart.model.CreateCartResp;
import com.active.services.cart.model.DeleteCartReq;
import com.active.services.cart.model.DeleteCartResp;
import com.active.services.cart.model.GetCartReq;
import com.active.services.cart.model.GetCartResp;
import com.active.services.cart.model.RemoveItemFromCartReq;
import com.active.services.cart.model.RemoveItemFromCartResp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping(path = "/api/cart/", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PersistedCartController {
    private final CartService cartService;

    @GetMapping(value = "/cart", consumes = "application/vnd.active.cart-service.v1+json")
    public GetCartResp getCart(@RequestBody @Valid GetCartReq request) {
        return null;
    }

    @PostMapping(value = "/cart", consumes = "application/vnd.active.cart-service.v1+json")
    public CreateCartResp createCart(@RequestBody @Valid CreateCartReq request) {
        return null;
    }

    @PutMapping(value = "/cart", consumes = "application/vnd.active.cart-service.v1+json")
    public AddItemToCartResp addItemToCart(@RequestBody @Valid AddItemToCartReq request) {
        return null;
    }

    @PutMapping(value = "/cart", consumes = "application/vnd.active.cart-service.v1+json")
    public ApplyDiscountToCartResp applyDiscountToCart(@RequestBody @Valid ApplyDiscountToCartReq request) {
        return null;
    }

    @DeleteMapping(value = "/cart", consumes = "application/vnd.active.cart-service.v1+json")
    public RemoveItemFromCartResp removeItemFromCart(@RequestBody @Valid RemoveItemFromCartReq request) {
        return null;
    }

    @DeleteMapping(value = "/cart", consumes = "application/vnd.active.cart-service.v1+json")
    public DeleteCartResp deleteCart(@RequestBody @Valid DeleteCartReq request) {
        return null;
    }
}
