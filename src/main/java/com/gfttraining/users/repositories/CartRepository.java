package com.gfttraining.users.repositories;

import com.gfttraining.users.exceptions.CartConnectionRefusedException;
import com.gfttraining.users.exceptions.CartResponseFailedException;
import com.gfttraining.users.models.Cart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Data
@Repository
@AllArgsConstructor
@NoArgsConstructor
public class CartRepository {
    private RestTemplate restTemplate;

    public CartRepository(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${cartMicroservice.url}")
    public String externalServiceUrl;

    public List<Cart> getCartById(Long user) throws CartConnectionRefusedException, CartResponseFailedException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Long> requestEntity = new HttpEntity<>(user, headers);

        try{
            ResponseEntity<List<Cart>> responseEntity = restTemplate.exchange(
                    "http://"+ externalServiceUrl +":"+"8887"+"/carts/getCartsById/",
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<List<Cart>>() {}
            );

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return responseEntity.getBody();
            } else if(responseEntity.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR){
                throw new CartConnectionRefusedException("Invalid connection with cart microservice");
            } else{
                throw new CartResponseFailedException("Invalid cart response: Expected 200 Got: " + responseEntity.getStatusCode());
            }

        } catch (RestClientException ex){
            throw new CartConnectionRefusedException("Invalid connection with cart microservice");
        }
    }
}
