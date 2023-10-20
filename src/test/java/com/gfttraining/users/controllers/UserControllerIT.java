package com.gfttraining.users.controllers;

import com.gfttraining.users.models.PaymentMethod;
import com.gfttraining.users.models.User;
import com.gfttraining.users.models.UserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIT {
//anyassert
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Create User")
    public void testCreateUser() {
        UserRequest userRequest = new UserRequest(
                "John",
                "Doe",
                "123 Main St",
                "City",
                "Province",
                12345,
                "Spain",
                "PayPal",
                100,
                75.0
        );

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserRequest> requestEntity = new HttpEntity<>(userRequest, headers);

        ResponseEntity<User> responseEntity = restTemplate.exchange("/users", HttpMethod.POST, requestEntity, User.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        User createdUser = responseEntity.getBody();

        assertNotNull(createdUser);
        assertEquals(createdUser.getName(), "John");
        assertEquals(createdUser.getLastName(), "Doe");
        assertEquals(createdUser.getAddress().getStreet(), "123 Main St");
        assertEquals(createdUser.getAddress().getCity(), "City");
        assertEquals(createdUser.getAddress().getProvince(), "Province");
        assertEquals(createdUser.getAddress().getPostalCode(), 12345);
        assertEquals(createdUser.getAddress().getCountry().getName(), "Spain");
        assertEquals(createdUser.getPaymentMethod().getName(), "PayPal");
        assertEquals(createdUser.getFidelityPoints(), 100);
        assertEquals(createdUser.getAveragePurchase(), 75.0);
    }

    @Test
    @DisplayName("Update User by ID")
    public void testUpdateUserById() {
        long userId = 1L;

        UserRequest userRequest = new UserRequest(
                "John",
                "Doe",
                "123 Main St",
                "City",
                "Province",
                12345,
                "Spain",
                "PayPal",
                100,
                75.0
        );

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserRequest> requestEntity = new HttpEntity<>(userRequest, headers);

        ResponseEntity<User> responseEntity = restTemplate.exchange("/users/" + userId, HttpMethod.PUT, requestEntity, User.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        User updatedUser = responseEntity.getBody();
        assertEquals(updatedUser.getName(), "John");
        assertEquals(updatedUser.getLastName(), "Doe");
        assertEquals(updatedUser.getAddress().getStreet(), "123 Main St");
        assertEquals(updatedUser.getAddress().getCity(), "City");
        assertEquals(updatedUser.getAddress().getProvince(), "Province");
        assertEquals(updatedUser.getAddress().getPostalCode(), 12345);
        assertEquals(updatedUser.getAddress().getCountry().getName(), "Spain");
        assertEquals(updatedUser.getPaymentMethod().getName(), "PayPal");
        assertEquals(updatedUser.getFidelityPoints(), 100);
        assertEquals(updatedUser.getAveragePurchase(), 75.0);

    }

    @Test
    @DisplayName("Update User by ID - Negative Test")
    public void testUpdateUserByIdNegative() {
        long nonExistentUserId = 9999L;

        UserRequest updatedUserRequest = new UserRequest("UpdatedName", "UpdatedLastName", "UpdatedAddress", "UpdatedCity", "UpdatedProvince", 54321, "UpdatedCountry", "UpdatedPaymentMethod", 300, 90.0);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserRequest> requestEntity = new HttpEntity<>(updatedUserRequest, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange("/users/" + nonExistentUserId, HttpMethod.PUT, requestEntity, String.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        assertEquals("User not found", responseEntity.getBody());
    }

    @Test
    @DisplayName("Get User by id")
    public void testGetUserById() {
        long userId = 1L;

        ResponseEntity<User> responseEntity = restTemplate.getForEntity("/users/" + userId, User.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        User user = responseEntity.getBody();

        assertNotNull(user);

        assertEquals(userId, user.getId());

        assertEquals("John", user.getName());
        assertEquals("Doe", user.getLastName());
        assertEquals("Sunset Blvd", user.getAddress().getStreet());
        assertEquals("Barcelona", user.getAddress().getCity());
        assertEquals("Catalonia", user.getAddress().getProvince());
        assertEquals(12345, user.getAddress().getPostalCode());
        assertEquals("Spain", user.getAddress().getCountry());
        assertEquals("Credit Card", user.getPaymentMethod().getName());
        assertEquals(100, user.getFidelityPoints());
        assertEquals(75.50, user.getAveragePurchase(), 0.001);
    }

    @Test
    @DisplayName("Load List of Users")
    public void testLoadListOfUsers() {
        List<UserRequest> userRequestList = new ArrayList<>();
        userRequestList.add(new UserRequest("John", "Doe", "123 Main St", "City", "Province", 12345, "Country", "PayPal", 100, 75.0));
        userRequestList.add(new UserRequest("Alice", "Johnson", "456 Elm St", "Another City", "Another Province", 54321, "Another Country", "Credit Card", 200, 50.0));

        HttpEntity<List<UserRequest>> requestEntity = new HttpEntity<>(userRequestList);

        ParameterizedTypeReference<List<User>> responseType = new ParameterizedTypeReference<List<User>>() {};
        ResponseEntity<List<User>> responseEntity = restTemplate.exchange("/users/load", HttpMethod.POST, requestEntity, responseType);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        List<User> savedUsers = responseEntity.getBody();

        assertNotNull(savedUsers);

        assertEquals("John", savedUsers.get(0).getName());
        assertEquals("Doe", savedUsers.get(0).getLastName());
        assertEquals("123 Main St", savedUsers.get(0).getAddress().getStreet());
        assertEquals("City", savedUsers.get(0).getAddress().getCity());
        assertEquals("Province", savedUsers.get(0).getAddress().getProvince());
        assertEquals(12345, savedUsers.get(0).getAddress().getPostalCode());
        assertEquals("Country", savedUsers.get(0).getAddress().getCountry());
        assertEquals("PayPal", savedUsers.get(0).getPaymentMethod().getName());
        assertEquals(100, savedUsers.get(0).getFidelityPoints());
        assertEquals(75.0, savedUsers.get(0).getAveragePurchase());

        assertEquals("Alice", savedUsers.get(1).getName());
        assertEquals("Johnson", savedUsers.get(1).getLastName());
        assertEquals("456 Elm St", savedUsers.get(1).getAddress().getStreet());
        assertEquals("Another City", savedUsers.get(1).getAddress().getCity());
        assertEquals("Another Province", savedUsers.get(1).getAddress().getProvince());
        assertEquals(54321, savedUsers.get(1).getAddress().getPostalCode());
        assertEquals("Another Country", savedUsers.get(1).getAddress().getCountry());
        assertEquals("Credit Card", savedUsers.get(1).getPaymentMethod().getName());
        assertEquals(200, savedUsers.get(1).getFidelityPoints());
        assertEquals(50.0, savedUsers.get(1).getAveragePurchase());
    }

    @Test
    @DisplayName("List of Users")
    public void testGetListOfUsers() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/users/list", String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        String responseBody = responseEntity.getBody();
    }

    @Test
    @DisplayName("Delete User by ID")
    public void testDeleteUserById() {
        long userIdToDelete = 1L;

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange("/users/" + userIdToDelete, HttpMethod.DELETE, requestEntity, String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertEquals("User deleted successfully", responseEntity.getBody());
    }

}
