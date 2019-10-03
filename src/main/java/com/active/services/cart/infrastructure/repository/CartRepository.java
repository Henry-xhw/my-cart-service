package com.active.services.cart.infrastructure.repository;

import com.active.services.cart.domain.cart.Cart;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends CrudRepository<Cart, Long> {
}
