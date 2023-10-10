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
    @DisplayName("Testing that a User entity can be created")
    void testCreateUser() {
        ResponseEntity<User> expectedResponse = new ResponseEntity<>(testUser, HttpStatus.CREATED);
        when(userService.createUser(testUser)).thenReturn(expectedResponse);

        ResponseEntity<User> responseEntity = userController.createUser(testUser);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(testUser, responseEntity.getBody());

        verify(userService, times(1)).createUser(testUser);

        System.out.println("User created: " + testUser.getName());
    }
}