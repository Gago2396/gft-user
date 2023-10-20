package com.gfttraining.users.services;

import com.gfttraining.users.controllers.UserController;
import com.gfttraining.users.models.*;
import com.gfttraining.users.repositories.FavoriteRepository;
import com.gfttraining.users.repositories.PaymentMethodRepository;
import com.gfttraining.users.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserController userController;

    @Mock
    private PaymentMethodService paymentMethodService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    private UserRequest userRequest;

    private UserRequest updatedUserRequest;

    private User testUser;

    private User updatedTestUser;

    private Country updatedCountry;

    private Address updatedAddress;

    private PaymentMethod paymentMethod;

    @BeforeEach
    void setUp() {
        userController = new UserController(userService);

        // PaymentMethod
        paymentMethod = new PaymentMethod();
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
        updatedUserRequest.setPaymentMethod("PayPal");
        updatedUserRequest.setFidelityPoints(400);
        updatedUserRequest.setAveragePurchase(150.5);
    }

    @Test
    void findPaymentMethod() {
        String paymentMethodName = paymentMethod.getName();

        when(paymentMethodService.getPaymentMethodByName(paymentMethodName)).thenReturn(Optional.ofNullable(paymentMethod));

        Optional<PaymentMethod> result = paymentMethodService.getPaymentMethodByName(paymentMethodName);

        assertThat(result.get().getName(), equalTo("PayPal"));
        assertThat(result.get().getId(), equalTo(1L));

        assertThat(result.get(), equalTo(paymentMethod));

        verify(paymentMethodService, times(1)).getPaymentMethodByName(paymentMethodName);
    }

    @Test
    void createUser() {
    }

    @Test
    void deleteUserById() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        doNothing().when(favoriteRepository).deleteByUser(testUser);

        userService.deleteUserById(userId);

        verify(userRepository, times(1)).findById(userId);

        verify(favoriteRepository, times(1)).deleteByUser(testUser);

        verify(userRepository, times(1)).deleteById(userId);
    }

//    @Test
//    void updateUserById() {
//        long userId = 1L;
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
//
//        when(userService.parseUser(updatedUserRequest)).thenReturn(updatedTestUser);
//
//        when(userRepository.save(updatedTestUser)).thenReturn(updatedTestUser);
//
//        User result = userService.updateUserById(userId, updatedUserRequest);
//
//        verify(userRepository, times(1)).findById(userId);
//
//        verify(userService, times(1)).parseUser(updatedUserRequest);
//
//        verify(userRepository, times(1)).save(updatedTestUser);
//
//        assertEquals(updatedTestUser, result);
//    }


    @Test
    void parseUser() {
    }

//    @Test
//    void loadListOfUsers() {
//        List<UserRequest> userRequestList = Arrays.asList(userRequest, userRequest, userRequest); // Crea una lista de solicitudes de usuario de ejemplo
//
//        List<User> userList = Arrays.asList(testUser, testUser, testUser);
//        when(userService.parseUser(userRequest)).thenReturn(testUser);
//
//        when(userRepository.saveAll(userList)).thenReturn(userList);
//
//        List<User> result = userService.loadListOfUsers(userRequestList);
//
//        verify(userService, times(3)).parseUser(userRequest);
//
//        verify(userRepository, times(1)).saveAll(userList);
//
//        assertEquals(userList, result);
//    }


    @Test
    void getUserById() {
    }

    @Test
    void getUserByName() {
    }

    @Test
    void getListOfUsers() {
    }
}