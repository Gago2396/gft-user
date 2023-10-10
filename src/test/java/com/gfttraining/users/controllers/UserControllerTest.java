package com.gfttraining.users.controllers;

import com.gfttraining.users.models.PaymentMethod;
import com.gfttraining.users.models.User;
import com.gfttraining.users.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(1L);
        paymentMethod.setName("Credit Card");

        testUser = new User();
        testUser.setId(1L);
        testUser.setName("John");
        testUser.setLastName("Doe");
        testUser.setAddress("123 Main St");
        testUser.setPaymentMethod(paymentMethod);
        testUser.setFidelityPoints(100);
        testUser.setAveragePurchase(50.0);
    }

    @Test
    @DisplayName("Testing that a User can be created")
    void testCreateUser() {
        ResponseEntity<User> expectedResponse = new ResponseEntity<>(testUser, HttpStatus.CREATED);
        when(userService.createUser(testUser)).thenReturn(expectedResponse);

        ResponseEntity<User> responseEntity = userController.createUser(testUser);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(testUser, responseEntity.getBody());

        verify(userService, times(1)).createUser(testUser);

        System.out.println("User created: " + testUser.getName());
    }

    @Test
    @DisplayName("Testing that a User can be updated by a given Id")
    void testUpdateUserById() {
        long userId = 1L;

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setName("Updated John");
        updatedUser.setLastName("Updated Doe");

        ResponseEntity<User> expectedResponse = new ResponseEntity<>(updatedUser, HttpStatus.OK);

        when(userService.updateUserById(userId, updatedUser)).thenReturn(expectedResponse);

        ResponseEntity<User> responseEntity = userController.updateUserById(userId, updatedUser);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedUser, responseEntity.getBody());

        verify(userService, times(1)).updateUserById(userId, updatedUser);

        System.out.println("User updated: " + updatedUser.getName());
    }

    @Test
    @DisplayName("Testing to load a list of Users")
    void testLoadListOfUsers() {
        List<User> userList = Arrays.asList(testUser,testUser,testUser);

        when(userService.loadListOfUsers()).thenReturn(new ResponseEntity<>(userList, HttpStatus.OK));

        ResponseEntity<List<User>> responseEntity = userController.loadListOfUsers();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userList, responseEntity.getBody());

        verify(userService, times(1)).loadListOfUsers();

        System.out.println("Users loaded to DB: " + userList.size());
    }
}
