package com.gfttraining.users.services;

import com.gfttraining.users.exceptions.CartConnectionRefusedException;
import com.gfttraining.users.exceptions.CartResponseFailedException;
import com.gfttraining.users.models.Cart;
import com.gfttraining.users.models.Status;
import com.gfttraining.users.repositories.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class CartRepositoryTest {
    @Mock
    RestTemplate restTemplate;

    private CartRepository cartRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cartRepository = new CartRepository(restTemplate);
    }

    @Test
    void getCartsByIdTest() throws CartConnectionRefusedException, CartResponseFailedException {

        Cart cart1 = new Cart(1L, 1L, Status.SUBMITTED, new BigDecimal("123.3"), new BigDecimal("4.5"));
        Cart cart2 = new Cart(2L, 1L, Status.DRAFT, new BigDecimal("343.3"), new BigDecimal("23.5"));

        long user = 1L;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<Cart> cartList = Arrays.asList(cart1, cart2);

        HttpEntity<Long> requestEntity = new HttpEntity<>(user, headers);

        ResponseEntity<List<Cart>> response = new ResponseEntity<>(cartList, HttpStatus.OK);

        when(restTemplate.exchange(
                "http://"+ cartRepository.externalServiceUrl +":"+"8887"+"/carts/getCartsById/",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<Cart>>() {}
        )).thenReturn(response);

        List<Cart> cartsResponse = cartRepository.getCartById(user);

        assertEquals(cartsResponse, response.getBody());

    }

    @Test
    void CartConnectionRefusedExceptionTest() throws CartResponseFailedException, CartConnectionRefusedException {
        Cart cart1 = new Cart(1L, 1L, Status.SUBMITTED, new BigDecimal("123.3"), new BigDecimal("4.5"));
        Cart cart2 = new Cart(2L, 1L, Status.DRAFT, new BigDecimal("343.3"), new BigDecimal("23.5"));

        long user = 1L;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<Cart> cartList = Arrays.asList(cart1, cart2);

        HttpEntity<Long> requestEntity = new HttpEntity<>(user, headers);

        ResponseEntity<List<Cart>> response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        when(restTemplate.exchange(
                "http://"+ cartRepository.externalServiceUrl +":"+"8887"+"/carts/getCartsById/",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<Cart>>() {}
        )).thenReturn(response);

        assertThrows(CartConnectionRefusedException.class, () -> cartRepository.getCartById(user));
    }

    @Test
    void CartResponseFailedExceptionTest() throws CartResponseFailedException, CartConnectionRefusedException {
        Cart cart1 = new Cart(1L, 1L, Status.SUBMITTED, new BigDecimal("123.3"), new BigDecimal("4.5"));
        Cart cart2 = new Cart(2L, 1L, Status.DRAFT, new BigDecimal("343.3"), new BigDecimal("23.5"));

        long user = 1L;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<Cart> cartList = Arrays.asList(cart1, cart2);

        HttpEntity<Long> requestEntity = new HttpEntity<>(user, headers);

        ResponseEntity<List<Cart>> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        when(restTemplate.exchange(
                "http://"+ cartRepository.externalServiceUrl +":"+"8887"+"/carts/getCartsById/",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<Cart>>() {}
        )).thenReturn(response);

        assertThrows(CartResponseFailedException.class, () -> cartRepository.getCartById(user));
    }

    @Test
    void RestClientExceptionTest() throws CartResponseFailedException, CartConnectionRefusedException {

        long user = 1L;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Long> requestEntity = new HttpEntity<>(user, headers);

        when(restTemplate.exchange(
                "http://"+ cartRepository.externalServiceUrl +":"+"8887"+"/carts/getCartsById/",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<Cart>>() {}
        )).thenThrow(RestClientException.class);

        assertThrows(CartConnectionRefusedException.class, () -> cartRepository.getCartById(user));
    }

}
