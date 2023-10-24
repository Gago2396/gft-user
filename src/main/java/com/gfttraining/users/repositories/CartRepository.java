package com.gfttraining.users.repositories;


import com.gfttraining.users.models.Cart;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Repository
public class CartRepository {


    private final RestTemplate restTemplate;
    @Value("${cartMicroservice.url}")
    public String externalServiceUrl;

    public CartRepository(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

    }
//    @Retryable(retryFor = InvalidCartConnectionException.class, maxAttemptsExpression = "${retry.maxAttempts}",
//            backoff = @Backoff(delayExpression = "${retry.maxDelay}"))

    public List<Cart> getCartsById(Long id) throws InvalidCartConnectionException, InvalidCartResponseException {
        System.out.println("Attempt");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Long> requestEntity = new HttpEntity<>(id, headers);

        try{
            ResponseEntity<Void> responseEntity = restTemplate.exchange(
                    "http://"+ externalServiceUrl +":"+"8887"+"/carts/getCartsById/",
                    HttpMethod.PUT,
                    requestEntity,
                    Void.class
            );

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return responseEntity.getBody();
            } else if(responseEntity.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR){
                throw new InvalidCartConnectionException("Invalid connection with cart microservice");
            } else{
                throw new InvalidCartResponseException("Invalid cart response: Expected 200 Got: " + responseEntity.getStatusCode());
            }

        } catch (RestClientException ex){
            throw new InvalidCartConnectionException("Invalid connection with cart microservice");
        }



    }

}
