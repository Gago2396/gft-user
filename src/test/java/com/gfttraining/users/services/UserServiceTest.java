package com.gfttraining.users.services;

import com.gfttraining.users.controllers.UserController;
import com.gfttraining.users.exceptions.CountryNotFoundException;
import com.gfttraining.users.models.*;
import com.gfttraining.users.repositories.FavoriteRepository;
import com.gfttraining.users.repositories.PaymentMethodRepository;
import com.gfttraining.users.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.gfttraining.users.exceptions.NoUsersWithThatNameException;
import com.gfttraining.users.exceptions.PaymentMethodNotFoundException;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;

    @Mock
    private UserController userController;

    @Mock
    private PaymentMethodService paymentMethodService;

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private CountryService countryService;

    @Mock
    private AddressService addressService;

    private UserRequest userRequest;

    private UserRequest updatedUserRequest;

    private User testUser;

    private User updatedTestUser;

    private Country country;

    private Address address;

    private Country updatedCountry;

    private Address updatedAddress;

    private PaymentMethod paymentMethod;

    private CartService cartService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, countryService, addressService, paymentMethodService, favoriteRepository, cartService);
        userController = new UserController(userService);

        // PaymentMethod
        paymentMethod = new PaymentMethod();
        paymentMethod.setId(1L);
        paymentMethod.setName("PayPal");

        // Country
        country = new Country();
        country.setName("Spain");

        // Updated Country
        updatedCountry = new Country();
        country.setName("Estonia");

        // Address
        address = new Address();
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
        userRequest.setPaymentMethod("PayPal");
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
    @DisplayName("GIVEN a valid UserRequest WHEN save method is called THEN save an User and return an User and CREATED")
    void createUser() {
        when(paymentMethodService.getPaymentMethodByName(userRequest.getPaymentMethod())).thenReturn(Optional.ofNullable(paymentMethod));
        when(countryService.getCountryByName(userRequest.getCountry())).thenReturn(Optional.ofNullable(country));

        Country userCountry = countryService.getCountryByName(userRequest.getCountry()).get();
        AddressRequest addressRequest = new AddressRequest(userRequest.getStreet(), userRequest.getCity(), userRequest.getProvince(), userRequest.getPostalCode(), userCountry);
        when(addressService.parseAddress(addressRequest)).thenReturn(address);

        Optional<PaymentMethod> testPaymentMethod = paymentMethodService.getPaymentMethodByName(userRequest.getPaymentMethod());

        Address testAddress = addressService.parseAddress(addressRequest);

        User parsedUser = new User();
        parsedUser.setName(userRequest.getName());
        parsedUser.setLastName(userRequest.getLastName());
        parsedUser.setAddress(testAddress);
        parsedUser.setFidelityPoints(userRequest.getFidelityPoints());
        parsedUser.setAveragePurchase(userRequest.getAveragePurchase());
        parsedUser.setPaymentMethod(testPaymentMethod.get());

        assertEquals("Antonio", parsedUser.getName());
        assertEquals("Garcia", parsedUser.getLastName());
        assertEquals("23 Mayor Updated", parsedUser.getAddress().getStreet());
        assertEquals("Valencia Updated", parsedUser.getAddress().getCity());
        assertEquals("PayPal", parsedUser.getPaymentMethod().getName());

        verify(paymentMethodService, times(1)).getPaymentMethodByName(userRequest.getPaymentMethod());
        verify(countryService, times(1)).getCountryByName(userRequest.getCountry());
    }

    @Test
    @DisplayName("GIVEN a valid User ID WHEN deleteUserById method is called THEN delete a User")
    void deleteUserById() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        doNothing().when(favoriteRepository).deleteByUser(testUser);

        userService.deleteUserById(userId);

        verify(userRepository, times(1)).findById(userId);

        verify(favoriteRepository, times(1)).deleteByUser(testUser);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("GIVEN an invalid User ID WHEN deleteUserById method is called THEN throw NoSuchElementException")
    void deleteUserByIdError() {
        Long userId = 1234L;

        Throwable exception = assertThrows(NoSuchElementException.class, () -> userService.deleteUserById(userId));

        assertEquals("User not found", exception.getMessage());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("GIVEN a valid User ID WHEN deleteUserById method is called THEN delete a User")
    void updateUserById() {
        long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(paymentMethodService.getPaymentMethodByName(userRequest.getPaymentMethod())).thenReturn(Optional.ofNullable(paymentMethod));
        when(countryService.getCountryByName(userRequest.getCountry())).thenReturn(Optional.ofNullable(country));

        AddressRequest addressRequest = new AddressRequest(userRequest.getStreet(), userRequest.getCity(), userRequest.getProvince(), userRequest.getPostalCode(), country);
        when(addressService.parseAddress(addressRequest)).thenReturn(address);

        userRepository.findById(userId);
        User resultParsedUser = userService.parseUser(userRequest);

        resultParsedUser.setId(userId);
        userRepository.save(resultParsedUser);

        assertEquals("Antonio", resultParsedUser.getName());
        assertEquals("Garcia", resultParsedUser.getLastName());
        assertEquals("23 Mayor Updated", resultParsedUser.getAddress().getStreet());
        assertEquals("Valencia Updated", resultParsedUser.getAddress().getCity());
        assertEquals("PayPal", resultParsedUser.getPaymentMethod().getName());

        verify(paymentMethodService, times(1)).getPaymentMethodByName(userRequest.getPaymentMethod());
        verify(countryService, times(1)).getCountryByName(userRequest.getCountry());
    }

    @Test
    @DisplayName("GIVEN a valid UserRequest WHEN parseUser method is called THEN return a User")
    void parseUser() {
        when(paymentMethodService.getPaymentMethodByName(userRequest.getPaymentMethod())).thenReturn(Optional.ofNullable(paymentMethod));
        when(countryService.getCountryByName(userRequest.getCountry())).thenReturn(Optional.ofNullable(country));

        Country userCountry = countryService.getCountryByName(userRequest.getCountry()).get();
        AddressRequest addressRequest = new AddressRequest(userRequest.getStreet(), userRequest.getCity(), userRequest.getProvince(), userRequest.getPostalCode(), userCountry);
        when(addressService.parseAddress(addressRequest)).thenReturn(address);

        Optional<PaymentMethod> testPaymentMethod = paymentMethodService.getPaymentMethodByName(userRequest.getPaymentMethod());

        Address testAddress = addressService.parseAddress(addressRequest);

        User parsedUser = new User();
        parsedUser.setName(userRequest.getName());
        parsedUser.setLastName(userRequest.getLastName());
        parsedUser.setAddress(testAddress);
        parsedUser.setFidelityPoints(userRequest.getFidelityPoints());
        parsedUser.setAveragePurchase(userRequest.getAveragePurchase());
        parsedUser.setPaymentMethod(testPaymentMethod.get());

        assertEquals("Antonio", parsedUser.getName());
        assertEquals("Garcia", parsedUser.getLastName());
        assertEquals("23 Mayor Updated", parsedUser.getAddress().getStreet());
        assertEquals("Valencia Updated", parsedUser.getAddress().getCity());
        assertEquals("PayPal", parsedUser.getPaymentMethod().getName());

        verify(paymentMethodService, times(1)).getPaymentMethodByName(userRequest.getPaymentMethod());
        verify(countryService, times(1)).getCountryByName(userRequest.getCountry());
    }

    @Test
    @DisplayName("GIVEN a UserRequest with an invalid paymentMethod WHEN parseUser method is called THEN throw a PaymentMethodNotFoundException")
    void parseUserErrorPaymentMethod() {
        Throwable exception = assertThrows(PaymentMethodNotFoundException.class, () -> userService.parseUser(userRequest));

        assertEquals("Payment method not valid", exception.getMessage());

        verify(paymentMethodService, times(1)).getPaymentMethodByName(userRequest.getPaymentMethod());
        verify(countryService, times(0)).getCountryByName(userRequest.getCountry());
    }

    @Test
    @DisplayName("GIVEN a UserRequest with an invalid country WHEN parseUser method is called THEN throw a CountryNotFoundException")
    void parseUserErrorCountry() {
        when(paymentMethodService.getPaymentMethodByName(userRequest.getPaymentMethod())).thenReturn(Optional.ofNullable(paymentMethod));

        Throwable exception = assertThrows(CountryNotFoundException.class, () -> userService.parseUser(userRequest));

        assertEquals("Country not found", exception.getMessage());

        verify(paymentMethodService, times(1)).getPaymentMethodByName(userRequest.getPaymentMethod());
        verify(countryService, times(1)).getCountryByName(userRequest.getCountry());
    }

    @Test
    @DisplayName("GIVEN a list of UserRequest WHEN loadListOfUsers method is called THEN save the list and return OK")
    void loadListOfUsers() {

        List<User> userList = Arrays.asList(testUser, testUser, testUser);
        List<UserRequest> userRequestList = Arrays.asList(userRequest, userRequest, userRequest);

        when(paymentMethodService.getPaymentMethodByName(userRequest.getPaymentMethod())).thenReturn(Optional.ofNullable(paymentMethod));
        when(countryService.getCountryByName(userRequest.getCountry())).thenReturn(Optional.ofNullable(country));

        AddressRequest addressRequest = new AddressRequest(userRequest.getStreet(), userRequest.getCity(), userRequest.getProvince(), userRequest.getPostalCode(), country);
        when(addressService.parseAddress(addressRequest)).thenReturn(address);
        when(userRepository.saveAll(userList)).thenReturn(userList);

        List<User> result = userService.loadListOfUsers(userRequestList);

        verify(paymentMethodService, times(3)).getPaymentMethodByName(userRequest.getPaymentMethod());
        verify(countryService, times(3)).getCountryByName(userRequest.getCountry());
        verify(userRepository, times(1)).saveAll(userList);

        assertEquals(userList, result);
    }

    @Test
    @DisplayName("GIVEN a valid User ID WHEN getUserById method is called THEN return a User")
    void getUserById() {
        long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(testUser));

        Optional<User> testIdUser = userRepository.findById(userId);
        assertEquals("Antonio", testIdUser.get().getName());
        assertEquals("Garcia", testIdUser.get().getLastName());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("GIVEN an invalid User ID WHEN findById method is called THEN throw NoSuchElementException")
    void getUserByIdError() {
        long userId = 1L;

        when(userRepository.findById(userId))
                .thenThrow(new NoSuchElementException("User not found"));

        Throwable exception = assertThrows(NoSuchElementException.class, () -> userRepository.findById(userId));

        assertEquals("User not found", exception.getMessage());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("GIVEN a valid User name WHEN getUserByName method is called THEN return OK and a User")
    void getUserByName() {
        String name = "Antonio";

        List<User> userList = Arrays.asList(testUser, testUser, testUser);
        when(userRepository.findByName(name)).thenReturn(Optional.of(userList));

        List<User> result = userService.getUserByName(name);

        User firstUser = result.stream().findFirst().orElse(null);

        assertEquals("Antonio", firstUser.getName());
        assertEquals("Garcia", firstUser.getLastName());
        assertEquals("Valencia Updated", firstUser.getAddress().getCity());
        assertEquals(3, userList.size());

        verify(userRepository, times(1)).findByName(name);
    }

    @Test
    @DisplayName("GIVEN an invalid User name WHEN getUserByName method is called THEN return empty list")
    void getUserByNameError() {
        String name = "NonExistentName";

        when(userRepository.findByName(name))
                .thenThrow(new NoUsersWithThatNameException("User not found"));

        Throwable exception = assertThrows(NoUsersWithThatNameException.class, () -> userService.getUserByName(name));

        assertEquals("User not found", exception.getMessage());

        verify(userRepository, times(1)).findByName(name);
    }

}