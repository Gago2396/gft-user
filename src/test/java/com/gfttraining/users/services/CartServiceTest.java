package com.gfttraining.users.services;

import com.gfttraining.users.exceptions.CartConnectionRefusedException;
import com.gfttraining.users.exceptions.CartResponseFailedException;
import com.gfttraining.users.models.Address;
import com.gfttraining.users.models.Cart;
import com.gfttraining.users.models.Status;
import com.gfttraining.users.repositories.CartRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    private CartService cartService;

    private List<Cart> cartList;

    @BeforeEach
    void setUp() {
        cartService = new CartService(cartRepository);

        Cart cart1 = new Cart(1L, 1L, Status.SUBMITTED, new BigDecimal("111.3"), new BigDecimal("4.5"));
        Cart cart2 = new Cart(2L, 1L, Status.DRAFT, new BigDecimal("33.3"), new BigDecimal("23.5"));
        Cart cart3 = new Cart(1L, 1L, Status.SUBMITTED, new BigDecimal("50.3"), new BigDecimal("4.5"));

        // Cart List
        cartList = new ArrayList<>();
        cartList.add(cart1);
        cartList.add(cart2);
        cartList.add(cart3);

    }

    @Test
    @DisplayName("GIVEN a userId WHEN calling to the carts microservice getCartsById THEN return the result of fidelityPoints")
    void getFidelityPoints() throws CartResponseFailedException, CartConnectionRefusedException {
        Long user = 1L;

        when(cartRepository.getCartById(user)).thenReturn(cartList);

        int result = cartService.getFidelityPoints(1L);

        assertEquals(15, result);

    }

    @ParameterizedTest
    @CsvSource({
            "19.99, 0",
            "25, 1",
            "35, 3",
            "45, 3",
            "75, 5",
            "150, 10",
            "152, 10"
    })
    void testCalculatePoints(String price, int expectedPoints) {
        int actualPoints = CartService.calculatePoints(new BigDecimal(price));
        assertEquals(expectedPoints, actualPoints);
    }
}