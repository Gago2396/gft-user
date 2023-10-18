package com.gfttraining.users.controllers;

import com.gfttraining.users.models.PaymentMethod;
import com.gfttraining.users.models.User;
import com.gfttraining.users.models.UserRequest;
import com.gfttraining.users.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

    private UserRequest testUserRequest;

    private User testUser;

    @BeforeEach
    void setUp() {
        userController = new UserController(userService);

        // PaymentMethod
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(1L);
        paymentMethod.setName("PayPal");

        // User
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("John");
        testUser.setLastName("Doe");
        testUser.setAddress("123 Main St");
        testUser.setPaymentMethod(paymentMethod);
        testUser.setFidelityPoints(100);
        testUser.setAveragePurchase(50.0);

        // UserRequest
        testUserRequest = new UserRequest();
        testUserRequest.setId(1L);
        testUserRequest.setName("John");
        testUserRequest.setLastName("Doe");
        testUserRequest.setAddress("123 Main St");
        testUserRequest.setPaymentMethod("PayPal");
        testUserRequest.setFidelityPoints(100);
        testUserRequest.setAveragePurchase(50.0);
    }

    @Test
    @DisplayName("Testing that a User can be created")
    void testCreateUser() {
        ResponseEntity<User> expectedResponse = new ResponseEntity<>(testUser, HttpStatus.CREATED);
        when(userService.createUser(testUserRequest)).thenReturn(expectedResponse.getBody());

        ResponseEntity<User> responseEntity = userController.createUser(testUserRequest);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(testUser, responseEntity.getBody());

        verify(userService, times(1)).createUser(testUserRequest);

        System.out.println("User created: " + testUser.getName());
    }

    //ToDo: Finish assert response when negative create is implemented
    @Disabled
    @Test
    @DisplayName("Testing that a User entity can give a INTERNAL_SERVER_ERROR while creating a USER")
    void testCreateUserError() {
        when(userService.createUser(testUserRequest)).thenReturn(null);

        ResponseEntity<User> responseEntity = userController.createUser(testUserRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        verify(userService, times(1)).createUser(testUserRequest);

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

        ResponseEntity<?> expectedResponse = new ResponseEntity<>(testUser, HttpStatus.OK);

        doNothing().when(userService).deleteUserById(userId);

        ResponseEntity<?> responseEntity = userController.deleteUserById(userId);

        assertEquals(expectedResponse.getStatusCode(), responseEntity.getStatusCode());

        assertNull(responseEntity.getBody());

        verify(userService, times(1)).deleteUserById(userId);

        System.out.println("User deleted with ID: " + userId);
    }

    //ToDo: Finish handle bad delete response and then implement this test
    @Disabled
    @Test
    @DisplayName("Testing that a User entity can give a INTERNAL_SERVER_ERROR while deleting a USER")
    void testDeleteUserByIdError() {
        Long userId = 1L;

        doNothing().when(userService).deleteUserById(userId);

        ResponseEntity<?> responseEntity = userController.deleteUserById(userId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        verify(userService, times(1)).deleteUserById(userId);

        System.out.println("User deletion failed as expected.");
    }

    @Test
    @DisplayName("Update User by id")
    void testUpdateUserById() {

        long userId = 1L;

        // User
        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setName("Updated John");
        updatedUser.setLastName("Updated Doe");

        // UserRequest
        UserRequest updatedUserRequest = new UserRequest();
        updatedUserRequest.setId(userId);
        updatedUserRequest.setName("Updated John");
        updatedUserRequest.setLastName("Updated Doe");

        ResponseEntity<User> expectedResponse = new ResponseEntity<>(updatedUser, HttpStatus.OK);

        when(userService.updateUserById(userId, updatedUserRequest)).thenReturn(expectedResponse.getBody());

        ResponseEntity<User> responseEntity = userController.updateUserById(userId, updatedUserRequest);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedUser, responseEntity.getBody());

        verify(userService, times(1)).updateUserById(userId, updatedUserRequest);

        System.out.println("User updated: " + updatedUser.getName());
    }

    //ToDo: Finish assert response when negative delete is implemented
    @Disabled
    @Test
    @DisplayName("Testing that a User entity can give a INTERNAL_SERVER_ERROR while deleting a USER")
    void testUpdateUserByIdError() {
        long userId = 1L;
        UserRequest updatedUser = new UserRequest();

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
        List<UserRequest> userRequestList = Arrays.asList(testUserRequest, testUserRequest, testUserRequest);

        List<User> userList = Arrays.asList(testUser, testUser, testUser);

        when(userService.loadListOfUsers(userRequestList)).thenReturn(userList);

        ResponseEntity<List<User>> responseEntity = userController.loadListOfUsers(userRequestList);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(userList, responseEntity.getBody());

        verify(userService, times(1)).loadListOfUsers(userRequestList);

        System.out.println("Users loaded to DB: " + userList.size());
    }

    @Test
    @DisplayName("Testing that a User entity can give a INTERNAL_SERVER_ERROR while loading a list of USERS")
    void testLoadListOfUsersError() {
        List<UserRequest> userRequestList = Arrays.asList(testUserRequest, testUserRequest, testUserRequest);

        when(userService.loadListOfUsers(userRequestList)).thenReturn(null);

        ResponseEntity<List<User>> responseEntity = userController.loadListOfUsers(userRequestList);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        verify(userService, times(1)).loadListOfUsers(userRequestList);

        System.out.println("Loading users failed as expected.");
    }

}