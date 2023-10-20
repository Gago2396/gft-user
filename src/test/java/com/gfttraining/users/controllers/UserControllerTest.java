package com.gfttraining.users.controllers;

import com.gfttraining.users.models.*;
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

    private UserRequest userRequest;

    private UserRequest updatedUserRequest;

    private User testUser;

    private User updatedTestUser;

    private Country updatedCountry;

    private Address updatedAddress;

    UserControllerTest() {
    }

    @BeforeEach
    void setUp() {
        userController = new UserController(userService);

        // PaymentMethod
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(1L);
        paymentMethod.setName("PayPal");

        // Country
        Country country = new Country();
        country.setName("Spain");

        // Updated Country
        updatedCountry = new Country();
        country.setName("Estonia");

        // Address
        Address address = new Address();
        address.setStreet("23 Mayor");
        address.setCity("Valencia");
        address.setProvince("Valencia");
        address.setPostalCode(46002);
        address.setCountry(country);

        // Updated Address
        updatedAddress = new Address();
        address.setStreet("23 Mayor Updated");
        address.setCity("Valencia Updated");
        address.setProvince("Valencia Updated");
        address.setPostalCode(46022);
        address.setCountry(updatedCountry);

        // User
        testUser = new User();
        testUser.setName("Antonio");
        testUser.setLastName("Garcia");
        testUser.setAddress(address);
        testUser.setPaymentMethod(paymentMethod);
        testUser.setFidelityPoints(300);
        testUser.setAveragePurchase(120.0);

        // Updated User
        updatedTestUser = new User();
        updatedTestUser.setName("Antonio Updated");
        updatedTestUser.setLastName("Garcia Updated");
        updatedTestUser.setAddress(address);
        updatedTestUser.setPaymentMethod(paymentMethod);
        updatedTestUser.setFidelityPoints(300);
        updatedTestUser.setAveragePurchase(120.0);

        // UserRequest
        userRequest = new UserRequest();
        userRequest.setName("Antonio");
        userRequest.setLastName("Garcia");
        userRequest.setStreet("23 Mayor");
        userRequest.setCity("Valencia");
        userRequest.setProvince("Valencia");
        userRequest.setPostalCode(46002);
        userRequest.setCountry("Spain");
        userRequest.setPaymentMethod("Credit Card");
        userRequest.setFidelityPoints(300);
        userRequest.setAveragePurchase(120.0);

        // Updated UserRequest
        updatedUserRequest = new UserRequest();
        updatedUserRequest.setName("Alexelcapo");
        updatedUserRequest.setLastName("Jimeno");
        updatedUserRequest.setStreet("45 Menor");
        updatedUserRequest.setCity("Madrid");
        updatedUserRequest.setProvince("Madrid");
        updatedUserRequest.setPostalCode(46123);
        updatedUserRequest.setCountry("Estonia");
        updatedUserRequest.setPaymentMethod("Paypal");
        updatedUserRequest.setFidelityPoints(400);
        updatedUserRequest.setAveragePurchase(150.5);
    }

    @Test
    @DisplayName("Testing that a User can be created")
    void testCreateUser() {
        ResponseEntity<User> expectedResponse = new ResponseEntity<>(testUser, HttpStatus.CREATED);
        when(userService.createUser(userRequest)).thenReturn(expectedResponse.getBody());

        ResponseEntity<?> responseEntity = userController.createUser(userRequest);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(testUser, responseEntity.getBody());

        verify(userService, times(1)).createUser(userRequest);

        System.out.println("User created: " + testUser.getName());
    }

    //ToDo: Finish assert response when negative create is implemented
    @Disabled
    @Test
    @DisplayName("Testing that a User entity can give a INTERNAL_SERVER_ERROR while creating a USER")
    void testCreateUserError() {
        when(userService.createUser(userRequest)).thenReturn(null);

        ResponseEntity<?> responseEntity = userController.createUser(userRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        verify(userService, times(1)).createUser(userRequest);

        System.out.println("User creation failed as expected.");
    }

    @Test
    @DisplayName("Get User by id")
    void testGetUserById() {
        long userId = 1L;

        when(userService.getUserById(userId)).thenReturn(testUser);

        ResponseEntity<?> responseEntity = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(testUser, responseEntity.getBody());

        verify(userService, times(1)).getUserById(userId);

        System.out.println("User found by ID: " + testUser.getId());
    }

    //ToDo: negative testGetUserById

    @Test
    @DisplayName("Testing that a User entity can be deleted by ID")
    void testDeleteUserById() {
        Long userId = 1L;

        ResponseEntity<?> expectedResponse = new ResponseEntity<>(testUser, HttpStatus.OK);

        doNothing().when(userService).deleteUserById(userId);

        ResponseEntity<?> responseEntity = userController.deleteUserById(userId);

        assertEquals(expectedResponse.getStatusCode(), responseEntity.getStatusCode());

        assertEquals(responseEntity.getBody(), "User deleted successfully");

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
    @DisplayName("Get User by name")
    void testGetUserByName() {
        String userName = "Antonio";

        when(userService.getUserByName(userName)).thenReturn(Arrays.asList(testUser, testUser, testUser));

        ResponseEntity<?> responseEntity = userController.getUserByName(userName);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Arrays.asList(testUser, testUser, testUser), responseEntity.getBody());

        verify(userService, times(1)).getUserByName(userName);

        System.out.println("User found by Name: " + testUser.getName());
    }

    @Test
    @DisplayName("Test search by name empty")
    void testGetUserByNameError() {
        String userName = "ErrorUser";

        when(userService.getUserByName(userName)).thenReturn(Arrays.asList());

        ResponseEntity<?> responseEntity = userController.getUserByName(userName);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Arrays.asList(), responseEntity.getBody());

        verify(userService, times(1)).getUserByName(userName);

        System.out.println("User not found so empty list was returned");
    }

    @Test
    @DisplayName("Update User by id")
    void testUpdateUserById() {

        long userId = 1L;

        ResponseEntity<User> expectedResponse = new ResponseEntity<>(updatedTestUser, HttpStatus.OK);

        when(userService.updateUserById(userId, updatedUserRequest)).thenReturn(expectedResponse.getBody());

        ResponseEntity<?> responseEntity = userController.updateUserById(userId, updatedUserRequest);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedTestUser, responseEntity.getBody());

        verify(userService, times(1)).updateUserById(userId, updatedUserRequest);

        System.out.println("User updated: " + updatedTestUser.getName());
    }

    //ToDo: Finish assert response when negative delete is implemented
    @Disabled
    @Test
    @DisplayName("Testing that a User entity can give a INTERNAL_SERVER_ERROR while deleting a USER")
    void testUpdateUserByIdError() {
        long userId = 1L;
        UserRequest updatedUser = new UserRequest();

        when(userService.updateUserById(userId, updatedUser)).thenReturn(null);

        ResponseEntity<?> responseEntity = userController.updateUserById(userId, updatedUser);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        verify(userService, times(1)).updateUserById(userId, updatedUser);

        System.out.println("User update failed as expected.");
    }

    @Test
    @DisplayName("Testing to load a list of Users")
    void testLoadListOfUsers() {
        List<UserRequest> userRequestList = Arrays.asList(userRequest, userRequest, userRequest);

        List<User> userList = Arrays.asList(testUser, testUser, testUser);

        when(userService.loadListOfUsers(userRequestList)).thenReturn(userList);

        ResponseEntity<?> responseEntity = userController.loadListOfUsers(userRequestList);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(userList, responseEntity.getBody());

        verify(userService, times(1)).loadListOfUsers(userRequestList);

        System.out.println("Users loaded to DB: " + userList.size());
    }

    @Test
    @DisplayName("Testing that a User entity can give a INTERNAL_SERVER_ERROR while loading a list of USERS")
    @Disabled
    void testLoadListOfUsersError() {
        List<UserRequest> userRequestList = Arrays.asList(userRequest, userRequest, userRequest);

        when(userService.loadListOfUsers(userRequestList)).thenReturn(null);

        ResponseEntity<?> responseEntity = userController.loadListOfUsers(userRequestList);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        verify(userService, times(1)).loadListOfUsers(userRequestList);

        System.out.println("Loading users failed as expected.");
    }

    @Test
    @DisplayName("Get list of users")
    void testGetListOfUsers() {
        when(userService.getListOfUsers()).thenReturn(Arrays.asList(testUser, testUser, testUser));

        ResponseEntity<?> responseEntity = userController.getListOfUsers();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Arrays.asList(testUser, testUser, testUser), responseEntity.getBody());

        verify(userService, times(1)).getListOfUsers();
    }
}
