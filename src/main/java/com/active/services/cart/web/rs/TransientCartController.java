package com.active.services.cart.web.rs;

import com.active.services.cart.application.CartService;
import com.active.services.cart.model.CartItem;
import com.active.services.cart.model.CartItemFact;
import com.active.services.cart.model.CreateCartRequest;
import com.active.services.cart.model.CreateCartResp;
import com.active.services.cart.model.KVFactPair;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

import javax.validation.Valid;


@Slf4j
@RestController
@RequestMapping(path = "/api/cart/transient", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TransientCartController {
    private final CartService cartService;

    @PostMapping(value = "/", consumes = "application/vnd.active.cart-service.v1+json")
    public CreateCartResp create(@RequestBody @Valid CreateCartRequest request) {
        request = new CreateCartRequest();
        List<KVFactPair> facts = new ArrayList<>();
        facts.add(new KVFactPair("pricingDt", LocalDate.now()));
        facts.add(new KVFactPair("pricingTime", LocalTime.now()));
        facts.add(new KVFactPair("weekday", LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault())));
        facts.add(new KVFactPair("age", ThreadLocalRandom.current().nextInt(1, 100)));
        facts.add(new KVFactPair("residency", String.valueOf(ThreadLocalRandom.current().nextInt(1, 2) % 2 == 0)));

        CartItem item = CartItem.builder()
                .productId(ThreadLocalRandom.current().nextLong())
                .quantity(ThreadLocalRandom.current().nextInt(100))
                .cartItemFact(new CartItemFact()
                        .setKvFactPairs(facts))
                .build();
        request.setCartItems(Collections.singletonList(item));
        cartService.createCart(request);
        return null;
    }
}
