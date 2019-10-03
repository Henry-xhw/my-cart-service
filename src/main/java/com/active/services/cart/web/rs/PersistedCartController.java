package com.active.services.cart.web.rs;

import com.active.services.cart.application.CartService;
import com.active.services.cart.model.CartDto;
import com.active.services.cart.model.CartItemDto;
import com.active.services.cart.model.CartResultDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = "application/vnd.active.cart-service.v1+json")
@RequiredArgsConstructor
public class PersistedCartController {
    private final CartService cartService;

    @GetMapping(value = "/carts/{identifier}")
    public CartResultDto getCart(@PathVariable UUID identifier) {
        return null;
    }

    @PostMapping(value = "/carts")
    public CartResultDto createCart(@RequestBody @Valid CartDto cart) {
        return null;
    }

    @PutMapping(value = "/carts/{identifier}")
    public CartResultDto addItemToCart(@PathVariable UUID identifier, @RequestBody @Valid CartItemDto item) {
        return null;
    }

    @PutMapping(value = "/carts/{identifier}/discount")
    public CartResultDto applyDiscountToCart(@PathVariable UUID identifier, @RequestBody List<String> coupons) {
        return null;
    }

    @PatchMapping(value = "/carts/{identifier}/{itemIdentifier}/{quantity}")
    public CartResultDto updateQuantity(@PathVariable UUID identifier, @PathVariable UUID itemIdentifier,
        @PathVariable Integer quantity) {
        return null;
    }

    @DeleteMapping(value = "/carts/{identifier}/{itemIdentifier}")
    public CartResultDto removeItemFromCart(@PathVariable UUID identifier, @PathVariable UUID itemIdentifier) {
        return null;
    }

}
