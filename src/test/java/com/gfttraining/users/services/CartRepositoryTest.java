package com.gfttraining.users.services;

import com.gfttraining.users.repositories.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
    void getCartsByIdTest() throws InvalidCartConnectionException, InvalidCartResponseException {
        long user = 1L;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Long> requestEntity = new HttpEntity<>(user, headers);

        ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.OK);

        when(restTemplate.exchange(
                "http://"+ cartRepository.externalServiceUrl +":"+"8887"+"/carts/updateStock/",
                HttpMethod.PUT,
                requestEntity,
                Void.class
        )).thenReturn(response);

        ProductDTO productDTOResponse = cartRepository.updateProduct(productDTO);

        assertEquals(productDTOResponse, productDTO);

    }
    @Test
    void updateProductExceptionTest() throws InvalidCartConnectionException {
        BigDecimal bd = new BigDecimal("10.00");
        BigDecimal roundedPrice = bd.setScale(2, RoundingMode.CEILING);
        ProductDTO productDTO = new ProductDTO(
                1L,
                roundedPrice,
                50,
                1.0
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ProductDTO> requestEntity = new HttpEntity<>(productDTO, headers);

        ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Mockito.when(restTemplate.exchange(
                "http://"+ cartRepository.externalServiceUrl +":"+"8887"+"/carts/updateStock/",
                HttpMethod.PUT,
                requestEntity,
                Void.class
        )).thenReturn(response);

        assertThrows(InvalidCartConnectionException.class,() ->cartRepository.updateProduct(productDTO));



    }
}
