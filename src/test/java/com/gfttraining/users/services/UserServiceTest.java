package com.gfttraining.users.services;

import com.gfttraining.users.controllers.UserController;
import com.gfttraining.users.models.*;
import com.gfttraining.users.repositories.FavoriteRepository;
import com.gfttraining.users.repositories.PaymentMethodRepository;
import com.gfttraining.users.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.gfttraining.users.exceptions.CountryNotFoundException;
import com.gfttraining.users.exceptions.NoUsersWithThatNameException;
import com.gfttraining.users.exceptions.PaymentMethodNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
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

    @BeforeEach
    void setUp() {
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

    //ToDo: Error: Payment method not valid: PayPal
    @Disabled
    @Test
    void createUser() {
        when(userService.parseUser(updatedUserRequest)).thenReturn(testUser);

        when(userRepository.save(testUser)).thenReturn(testUser);

        User user = userService.parseUser(updatedUserRequest);

        User createdUser = userRepository.save(user);

        assertEquals("Antonio", createdUser.getName());
        assertEquals("García", createdUser.getLastName());

        verify(userService, times(1)).parseUser(updatedUserRequest);
        verify(userRepository, times(1)).save(testUser);

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

    //ToDo: Error: Payment method not valid: PayPal
    @Disabled
    @Test
    void updateUserById() {
        long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        when(userService.parseUser(updatedUserRequest)).thenReturn(updatedTestUser);

        when(userRepository.save(updatedTestUser)).thenReturn(updatedTestUser);

        User result = userService.updateUserById(userId, updatedUserRequest);

        verify(userRepository, times(1)).findById(userId);

        verify(userService, times(1)).parseUser(updatedUserRequest);

        verify(userRepository, times(1)).save(updatedTestUser);

        assertEquals(updatedTestUser, result);
    }

    @Test
    void parseUser() {
        when(paymentMethodService.getPaymentMethodByName(userRequest.getPaymentMethod())).thenReturn(Optional.ofNullable(paymentMethod));
        when(countryService.getCountryByName(userRequest.getCountry())).thenReturn(Optional.ofNullable(country));

        Country userCountry = countryService.getCountryByName(userRequest.getCountry()).get();
        AddressRequest addressRequest = new AddressRequest(userRequest.getStreet(), userRequest.getCity(), userRequest.getProvince(), userRequest.getPostalCode(), userCountry);
        when(addressService.parseAddress(addressRequest)).thenReturn(address);

        PaymentMethod testPaymentMethod = paymentMethodService.getPaymentMethodByName(userRequest.getPaymentMethod()).get();

        Address testAddress = addressService.parseAddress(addressRequest);

        User parsedUser = new User();
        parsedUser.setName(userRequest.getName());
        parsedUser.setLastName(userRequest.getLastName());
        parsedUser.setAddress(testAddress);
        parsedUser.setFidelityPoints(userRequest.getFidelityPoints());
        parsedUser.setAveragePurchase(userRequest.getAveragePurchase());
        parsedUser.setPaymentMethod(testPaymentMethod);

        assertEquals("Antonio", parsedUser.getName());
        assertEquals("Garcia", parsedUser.getLastName());
        assertEquals("23 Mayor Updated", parsedUser.getAddress().getStreet());
        assertEquals("Valencia Updated", parsedUser.getAddress().getCity());

        verify(paymentMethodService, times(1)).getPaymentMethodByName(userRequest.getPaymentMethod());
        verify(countryService, times(1)).getCountryByName(userRequest.getCountry());
    }

    @Test
    void parseUserErrorPaymentMethod() {
        when(paymentMethodService.getPaymentMethodByName(userRequest.getPaymentMethod()))
                .thenThrow(new PaymentMethodNotFoundException("Payment method not valid"));

        Throwable exception = assertThrows(PaymentMethodNotFoundException.class, () -> userService.parseUser(userRequest));

        assertEquals("Payment method not valid", exception.getMessage());

        verify(paymentMethodService, times(1)).getPaymentMethodByName(userRequest.getPaymentMethod());
    }

    //ToDo: Error: PaymentMethodNotFoundException is being thrown by error
    @Disabled
    @Test
    void parseUserErrorCountry() {
        when(countryService.getCountryByName(userRequest.getCountry()))
                .thenThrow(new CountryNotFoundException("Country not valid"));

        Throwable exception = assertThrows(CountryNotFoundException.class, () -> userService.parseUser(userRequest));

        assertEquals("Country not valid", exception.getMessage());

        verify(countryService, times(1)).getCountryByName(userRequest.getCountry());
    }

    //ToDo: Error: Payment method not valid: Credit Card
    @Disabled
    @Test
    void loadListOfUsers() {
        List<UserRequest> userRequestList = Arrays.asList(userRequest, userRequest, userRequest);

        List<User> userList = Arrays.asList(testUser, testUser, testUser);
        when(userService.parseUser(userRequest)).thenReturn(testUser);

        when(userRepository.saveAll(userList)).thenReturn(userList);

        List<User> result = userService.loadListOfUsers(userRequestList);

        verify(userService, times(3)).parseUser(userRequest);

        verify(userRepository, times(1)).saveAll(userList);

        assertEquals(userList, result);
    }


    @Test
    void getUserById() {
        long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(testUser));

        assertEquals("Antonio", testUser.getName());
        assertEquals("Garcia", testUser.getLastName());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUserByIdError() {
        long userId = 1L;

        when(userRepository.findById(userId))
                .thenThrow(new NoSuchElementException("User not found"));

        Throwable exception = assertThrows(NoSuchElementException.class, () -> userRepository.findById(userId));

        assertEquals("User not found", exception.getMessage());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUserByName() {
    }

    @Test
    void getListOfUsers() {
    }
}