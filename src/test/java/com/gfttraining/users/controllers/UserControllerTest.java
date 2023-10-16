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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private UserController userController;

    @Mock
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        userController = new UserController(userService);

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
        when(userService.createUser(testUser)).thenReturn(expectedResponse.getBody());

        ResponseEntity<User> responseEntity = userController.createUser(testUser);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(testUser, responseEntity.getBody());

        verify(userService, times(1)).createUser(testUser);

        System.out.println("User created: " + testUser.getName());
    }

    @Test
    @DisplayName("Testing that a User entity can give a INTERNAL_SERVER_ERROR while creating a USER")
    void testCreateUserError() {
        when(userService.createUser(testUser)).thenReturn(null);

        ResponseEntity<User> responseEntity = userController.createUser(testUser);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        verify(userService, times(1)).createUser(testUser);

        System.out.println("User creation failed as expected.");
    }

    @Test
    @DisplayName("Get User by id")
    void testGetUserById() {
        long userId = 1L;

        when(userService.getUserById(userId)).thenReturn(Optional.of(testUser));

        ResponseEntity<User> responseEntity = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(testUser, responseEntity.getBody());

        verify(userService, times(1)).getUserById(userId);

        System.out.println("User found by ID: " + testUser.getId());
    }

    @Test
    @DisplayName("Testing that a User entity can be deleted by ID")
    void testDeleteUserById() {
        Long userId = 1L;

        ResponseEntity<User> expectedResponse = new ResponseEntity<>(testUser, HttpStatus.OK);

        when(userService.deleteUserById(userId)).thenReturn(expectedResponse.getBody());
        ResponseEntity<User> responseEntity = userController.deleteUserById(userId);

        assertEquals(expectedResponse.getStatusCode(), responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        verify(userService, times(1)).deleteUserById(userId);

        System.out.println("User deleted with ID: " + userId);
    }

    @Test
    @DisplayName("Testing that a User entity can give a INTERNAL_SERVER_ERROR while deleting a USER")
    void testDeleteUserByIdError() {
        Long userId = 1L;
        when(userService.deleteUserById(userId)).thenReturn(null);

        ResponseEntity<User> responseEntity = userController.deleteUserById(userId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        verify(userService, times(1)).deleteUserById(userId);

        System.out.println("User deletion failed as expected.");
    }

    @Test
    @DisplayName("Update User by id")
    void testUpdateUserById() {
        long userId = 1L;

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setName("Updated John");
        updatedUser.setLastName("Updated Doe");

        ResponseEntity<User> expectedResponse = new ResponseEntity<>(updatedUser, HttpStatus.OK);

        when(userService.updateUserById(userId, updatedUser)).thenReturn(expectedResponse.getBody());

        ResponseEntity<User> responseEntity = userController.updateUserById(userId, updatedUser);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedUser, responseEntity.getBody());

        verify(userService, times(1)).updateUserById(userId, updatedUser);

        System.out.println("User updated: " + updatedUser.getName());
    }

    @Test
    @DisplayName("Testing that a User entity can give a INTERNAL_SERVER_ERROR while deleting a USER")
    void testUpdateUserByIdError() {
        long userId = 1L;
        User updatedUser = new User();

        when(userService.updateUserById(userId, updatedUser)).thenReturn(null);

        ResponseEntity<User> responseEntity = userController.updateUserById(userId, updatedUser);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        verify(userService, times(1)).updateUserById(userId, updatedUser);

        System.out.println("User update failed as expected.");
    }

    @Test
    @DisplayName("Testing to load a list of Users")
    void testLoadListOfUsers() {
        List<User> userList = Arrays.asList(testUser, testUser, testUser);

        when(userService.loadListOfUsers()).thenReturn(userList);

        ResponseEntity<List<User>> responseEntity = userController.loadListOfUsers();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(userList, responseEntity.getBody());

        verify(userService, times(1)).loadListOfUsers();

        System.out.println("Users loaded to DB: " + userList.size());
    }

    @Test
    @DisplayName("Testing that a User entity can give a INTERNAL_SERVER_ERROR while loading a list of USERS")
    void testLoadListOfUsersError() {
        when(userService.loadListOfUsers()).thenReturn(null);

        ResponseEntity<List<User>> responseEntity = userController.loadListOfUsers();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        verify(userService, times(1)).loadListOfUsers();

        System.out.println("Loading users failed as expected.");
    }

}