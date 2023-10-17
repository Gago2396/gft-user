package com.gfttraining.users.controllers;

import com.gfttraining.users.models.User;
import com.gfttraining.users.models.UserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Create User")
    public void testCreateUser() {
        UserRequest userRequest = new UserRequest(1L, "John", "Doe", "123 Main St", "PayPal", 100, 75.0);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserRequest> requestEntity = new HttpEntity<>(userRequest, headers);

        ResponseEntity<User> responseEntity = restTemplate.exchange("/users", HttpMethod.POST, requestEntity, User.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        User createdUser = responseEntity.getBody();

        assertNotNull(createdUser);
        assertEquals(createdUser.getName(), "John");
        assertEquals(createdUser.getLastName(), "Doe");
        assertEquals(createdUser.getAddress(), "123 Main St");
        assertEquals(createdUser.getPaymentMethod().getName(), "PayPal");
        assertEquals(createdUser.getFidelityPoints(), 100);
        assertEquals(createdUser.getAveragePurchase(), 75.0);

    }

    //ToDo: Negative POST User
}
