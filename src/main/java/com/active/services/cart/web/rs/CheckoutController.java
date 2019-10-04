package com.active.services.cart.web.rs;

import com.active.services.cart.model.CartResultDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(path = "/api/checkout", produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = "application/vnd.active.cart-service-checkout.v1+json")
@RequiredArgsConstructor
public class CheckoutController {

    @PostMapping(value = "/{cartId}")
    public CartResultDto checkout(@PathVariable UUID cartId) {
        return null;
    }

    @GetMapping(value = "/payment-options/{cartId}")
    public CartResultDto getCartPaymentOptions(@PathVariable UUID cartId) {
        return null;
    }

    @GetMapping(value = "/payment-options/{cartId}/item/{cartItemId}")
    public CartResultDto getCartItemPaymentOptions(@PathVariable UUID cartId, @PathVariable UUID cartItemId) {
        return null;
    }
}
