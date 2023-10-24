package com.gfttraining.users.services;

import com.gfttraining.users.repositories.CartRepository;

public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }
}
