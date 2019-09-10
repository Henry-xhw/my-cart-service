package com.active.services.cart.web.rs;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.active.services.cart.application.CartService;
import com.active.services.cart.model.CreateCartsReq;
import com.active.services.cart.model.CreateCartsResp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping(path = "/api/cart/", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PersistedCartController {
    private final CartService cartService;

    @PostMapping(value = "/", consumes = "application/vnd.active.cart-service.v1+json")
    public CreateCartsResp create(@RequestBody @Valid CreateCartsReq request) {
        return null;
    }

    @PutMapping(value = "/", consumes = "application/vnd.active.cart-service.v1+json")
    public CreateCartsResp addItemToCart(@RequestBody @Valid CreateCartsReq request) {
        return null;
    }

    @PutMapping(value = "/", consumes = "application/vnd.active.cart-service.v1+json")
    public CreateCartsResp applyDiscountToCart(@RequestBody @Valid CreateCartsReq request) {
        return null;
    }

    @DeleteMapping(value = "/", consumes = "application/vnd.active.cart-service.v1+json")
    public CreateCartsResp removeItemFromCart(@RequestBody @Valid CreateCartsReq request) {
        return null;
    }

    @DeleteMapping(value = "/", consumes = "application/vnd.active.cart-service.v1+json")
    public CreateCartsResp deleteCart(@RequestBody @Valid CreateCartsReq request) {
        return null;
    }
}
