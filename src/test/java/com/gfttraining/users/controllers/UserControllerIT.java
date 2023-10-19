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

    @Test
    @DisplayName("Create User - Negative Test")
    public void testCreateUserNegative() {
        UserRequest userRequest = new UserRequest(1L, "John","Doe", "123 Main St", "PayPal", 100, 75.0);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserRequest> requestEntity = new HttpEntity<>(userRequest, headers);

        ResponseEntity<User> responseEntity = restTemplate.exchange("/users", HttpMethod.POST, requestEntity, User.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        assertNull(responseEntity.getBody());
    }

    @Test
    @DisplayName("Update User by ID")
    public void testUpdateUserById() {
        long userId = 1L;

        UserRequest updatedUserRequest = new UserRequest(2L, "Josh", "Dowe", "123 Main St", "PayPal", 100, 75.0);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserRequest> requestEntity = new HttpEntity<>(updatedUserRequest, headers);

        ResponseEntity<User> responseEntity = restTemplate.exchange("/users/" + userId, HttpMethod.PUT, requestEntity, User.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        User updatedUser = responseEntity.getBody();
        assertEquals(updatedUser.getName(), "Josh");
        assertEquals(updatedUser.getLastName(), "Dowe");
    }

    //ToDo: Negative POST User

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
        assertEquals("1234 Elm St", user.getAddress());
        assertEquals("Credit Card", user.getPaymentMethod().getName());
        assertEquals(100, user.getFidelityPoints());
        assertEquals(75.50, user.getAveragePurchase(), 0.001);
    }

    //ToDo: Negative GET User

    @Test
    @DisplayName("Load List of Users")
    public void testLoadListOfUsers() {
        List<UserRequest> userRequestList = new ArrayList<>();
        userRequestList.add(new UserRequest(2L,"John", "Doe", "123 Main St", "PayPal", 100, 75.0));
        userRequestList.add(new UserRequest(3L, "Alice", "Johnson", "456 Elm St", "Credit Card", 200, 50.0));

        HttpEntity<List<UserRequest>> requestEntity = new HttpEntity<>(userRequestList);

        ParameterizedTypeReference<List<User>> responseType = new ParameterizedTypeReference<List<User>>() {};
        ResponseEntity<List<User>> responseEntity = restTemplate.exchange("/users/load", HttpMethod.POST, requestEntity, responseType);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        List<User> savedUsers = responseEntity.getBody();

        assertNotNull(savedUsers);

        assertEquals("John", savedUsers.get(0).getName());
        assertEquals("Doe", savedUsers.get(0).getLastName());
        assertEquals("123 Main St", savedUsers.get(0).getAddress());
        assertEquals("PayPal", savedUsers.get(0).getPaymentMethod().getName());
        assertEquals(100, savedUsers.get(0).getFidelityPoints());
        assertEquals(75.0, savedUsers.get(0).getAveragePurchase());

        assertEquals("Alice", savedUsers.get(1).getName());
        assertEquals("Johnson", savedUsers.get(1).getLastName());
        assertEquals("456 Elm St", savedUsers.get(1).getAddress());
        assertEquals("Credit Card", savedUsers.get(1).getPaymentMethod().getName());
        assertEquals(200, savedUsers.get(1).getFidelityPoints());
        assertEquals(50.0, savedUsers.get(1).getAveragePurchase());
    }

    @Test
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
