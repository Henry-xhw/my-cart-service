package com.active.services.cart.web.rs;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.active.services.cart.application.CartService;
import com.active.services.cart.model.CartDto;
import com.active.services.cart.model.CartItemDto;
import com.active.services.cart.model.CartResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j @RestController
@RequestMapping(path = "/api/cart/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = "application/vnd.active.cart-service.v1+json")
@RequiredArgsConstructor public class PersistedCartController {
    private final CartService cartService;

    @GetMapping(value = "/carts/{identifier}") public CartResult getCart(@PathVariable String identifier) {
        return null;
    }

    @PostMapping(value = "/carts") public CartResult createCart(@RequestBody @Valid CartDto cart) {
        return null;
    }

    @PutMapping(value = "/carts/{identifier}")
    public CartResult addItemToCart(@PathVariable String identifier, @RequestBody @Valid CartItemDto item) {
        return null;
    }

    @PutMapping(value = "/carts/{identifier}/discount")
    public CartResult applyDiscountToCart(@PathVariable String identifier, @RequestBody List<String> coupons) {
        return null;
    }

    @PutMapping(value = "/carts/{identifier}/{itemIdentifier}/{quantity}")
    public CartResult updateQuantity(@PathVariable String identifier, @PathVariable String itemIdentifier,
        @PathVariable Integer quantity) {
        return null;
    }

    @DeleteMapping(value = "/carts/{identifier}/{itemIdentifier}")
    public CartResult removeItemFromCart(@PathVariable String identifier, @PathVariable String itemIdentifier) {
        return null;
    }

}
