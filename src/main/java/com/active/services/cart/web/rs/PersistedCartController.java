package com.active.services.cart.web.rs;

import com.active.services.cart.application.CartService;
import com.active.services.cart.model.CreateCartRequest;
import com.active.services.cart.model.CreateCartResp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Slf4j
@RestController
@RequestMapping(path = "/api/cart/", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PersistedCartController {
    private final CartService cartService;

    @PostMapping(value = "/", consumes = "application/vnd.active.cart-service.v1+json")
    public CreateCartResp create(@RequestBody @Valid CreateCartRequest request) {
        return null;
    }

    @PutMapping(value = "/", consumes = "application/vnd.active.cart-service.v1+json")
    public CreateCartResp addItemToCart(@RequestBody @Valid CreateCartRequest request) {
        return null;
    }

    @PutMapping(value = "/", consumes = "application/vnd.active.cart-service.v1+json")
    public CreateCartResp applyDiscountToCart(@RequestBody @Valid CreateCartRequest request) {
        return null;
    }

    @DeleteMapping(value = "/", consumes = "application/vnd.active.cart-service.v1+json")
    public CreateCartResp removeItemFromCart(@RequestBody @Valid CreateCartRequest request) {
        return null;
    }

    @DeleteMapping(value = "/", consumes = "application/vnd.active.cart-service.v1+json")
    public CreateCartResp deleteCart(@RequestBody @Valid CreateCartRequest request) {
        return null;
    }
}
