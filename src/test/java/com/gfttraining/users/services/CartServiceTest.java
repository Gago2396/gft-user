package com.gfttraining.users.services;

import com.gfttraining.users.models.Cart;
import com.gfttraining.users.repositories.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    private CartService cartService;

    @BeforeEach
    void setUp() {
        cartService = new CartService(cartRepository);
        List<Cart> carts = new ArrayList<>();
        Cart cart1 = new Cart();
        Cart cart2 = new Cart();
    }

    @Test
    @DisplayName("GIVEN a userId WHEN calling to the carts microservice getCartsById THEN return the result of fidelityPoints")
    void getFidelityPoints(){
        when()
    }
}