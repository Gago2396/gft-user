package com.gfttraining.users.services;

import com.gfttraining.users.controllers.UserController;
import com.gfttraining.users.exceptions.*;
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
    private CartService cartService;

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
        userService = new UserService(userRepository, countryService, addressService, paymentMethodService, favoriteRepository, cartService);
        userController = new UserController(userService);

        // PaymentMethod
        paymentMethod = new PaymentMethod();
        paymentMethod.setId(1L);
        paymentMethod.setName("PayPal");

        // Country
        country = new Country();
        country.setId(1L);
        country.setName("Spain");

        // Updated Country
        updatedCountry = new Country();
        updatedCountry.setId(2L);
        updatedCountry.setName("Estonia");

        // Address
        address = new Address();
        address.setId(1L);
        address.setStreet("23 Mayor");
        address.setCity("Valencia");
        address.setProvince("Valencia");
        address.setPostalCode(46002);
        address.setCountry(country);

        // Updated Address
        updatedAddress = new Address();
        updatedAddress.setId(2L);
        updatedAddress.setStreet("23 Mayor Updated");
        updatedAddress.setCity("Valencia Updated");
        updatedAddress.setProvince("Valencia Updated");
        updatedAddress.setPostalCode(46022);
        updatedAddress.setCountry(updatedCountry);

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
        updatedTestUser.setId(1L);
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
        updatedUserRequest.setName("Antonio Updated");
        updatedUserRequest.setLastName("Garcia Updated");
        updatedUserRequest.setStreet("23 Mayor");
        updatedUserRequest.setCity("Valencia");
        updatedUserRequest.setProvince("Valencia");
        updatedUserRequest.setPostalCode(46022);
        updatedUserRequest.setCountry("Spain");
        updatedUserRequest.setPaymentMethod("PayPal");
        updatedUserRequest.setFidelityPoints(300);
        updatedUserRequest.setAveragePurchase(120.0);
    }

    @Test
    void testCreateUser() {
        when(paymentMethodService.getPaymentMethodByName(userRequest.getPaymentMethod())).thenReturn(Optional.ofNullable(paymentMethod));
        when(countryService.getCountryByName(userRequest.getCountry())).thenReturn(Optional.ofNullable(country));
        when(userRepository.save(testUser)).thenReturn(testUser);

        AddressRequest addressRequest = new AddressRequest(userRequest.getStreet(), userRequest.getCity(), userRequest.getProvince(), userRequest.getPostalCode(), country);
        when(addressService.parseAddress(addressRequest)).thenReturn(address);

        User createdUser = userService.createUser(userRequest);

        assertNotNull(createdUser);
        assertEquals(testUser, createdUser);
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
    @DisplayName("GIVEN a valid User ID WHEN updateUserById method is called THEN update a User")
    void updateUserById() {
        long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(updatedTestUser));
        when(paymentMethodService.getPaymentMethodByName(updatedUserRequest.getPaymentMethod())).thenReturn(Optional.ofNullable(paymentMethod));
        when(countryService.getCountryByName(updatedUserRequest.getCountry())).thenReturn(Optional.ofNullable(country));
        when(userRepository.save(updatedTestUser)).thenReturn(updatedTestUser);

        AddressRequest addressRequest = new AddressRequest(updatedUserRequest.getStreet(), updatedUserRequest.getCity(), updatedUserRequest.getProvince(), updatedUserRequest.getPostalCode(), country);
        when(addressService.parseAddress(addressRequest)).thenReturn(address);

        User updatedUser = userService.updateUserById(userId, updatedUserRequest);

        assertEquals("Antonio Updated", updatedUser.getName());
        assertEquals("Garcia Updated", updatedUser.getLastName());
        assertEquals("23 Mayor", updatedUser.getAddress().getStreet());
        assertEquals("Valencia", updatedUser.getAddress().getCity());
        assertEquals("PayPal", updatedUser.getPaymentMethod().getName());

        verify(paymentMethodService, times(1)).getPaymentMethodByName(userRequest.getPaymentMethod());
        verify(countryService, times(1)).getCountryByName(userRequest.getCountry());
    }


    @Test
    @DisplayName("GIVEN a valid User ID WHEN deleteUserById method is called THEN delete a User")
    void updateUserByIdError() {
        long userId = 99999L;

        Throwable exception = assertThrows(NoSuchElementException.class, () -> userService.updateUserById(userId, updatedUserRequest));

        assertEquals("User not found", exception.getMessage());
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
        assertEquals("23 Mayor", parsedUser.getAddress().getStreet());
        assertEquals("Valencia", parsedUser.getAddress().getCity());
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

        User testIdUser = userService.getUserById(userId);
        assertEquals("Antonio", testIdUser.getName());
        assertEquals("Garcia", testIdUser.getLastName());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("GIVEN an invalid User ID WHEN findById method is called THEN throw NoSuchElementException")
    void getUserByIdError() {
        long userId = 1L;

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        Throwable exception = assertThrows(NoSuchElementException.class, () -> userService.getUserById(userId));

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
        assertEquals("Valencia", firstUser.getAddress().getCity());
        assertEquals(3, userList.size());

        verify(userRepository, times(1)).findByName(name);
    }

    @Test
    @DisplayName("GIVEN an invalid User name WHEN getUserByName method is called THEN throw NoUsersWithThatNameException")
    void getUserByNameError() {
        String name = "NonExistentName";

        when(userRepository.findByName(name))
                .thenThrow(new NoUsersWithThatNameException("User not found"));

        Throwable exception = assertThrows(NoUsersWithThatNameException.class, () -> userService.getUserByName(name));

        assertEquals("User not found", exception.getMessage());

        verify(userRepository, times(1)).findByName(name);
    }

    @Test
    @DisplayName("GIVEN a valid endpoint call WHEN getListOfUsers method is called THEN return a list of Users and OK")
    void testGetListOfUsers() {
        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(testUser);
        expectedUsers.add(testUser);
        expectedUsers.add(testUser);

        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> result = userService.getListOfUsers();

        assertEquals(expectedUsers, result);
    }

    @Test
    @DisplayName("GIVEN a valid User id WHEN updateUserFidelityPoints method is called THEN update its fidelity points")
    void testUpdateUserFidelityPoints() throws CartResponseFailedException, CartConnectionRefusedException, CartResponseFailedException, CartConnectionRefusedException {
        long userId = 1L;
        int simulatedFidelityPoints = 500;

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(cartService.getFidelityPoints(userId)).thenReturn(simulatedFidelityPoints);
        when(userRepository.save(testUser)).thenReturn(testUser);

        User updatedUser = userService.updateUserFidelityPoints(userId);
        System.out.println(updatedUser);
        verify(userRepository, times(1)).findById(1L);

        verify(cartService, times(1)).getFidelityPoints(1L);

        assertEquals(simulatedFidelityPoints, updatedUser.getFidelityPoints());

        verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    @DisplayName("GIVEN an invalid User ID WHEN findById method is called THEN throw NoSuchElementException")
    void getFidelityPointsError() {
        long userId = 1L;

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        Throwable exception = assertThrows(NoSuchElementException.class, () -> userService.updateUserFidelityPoints(userId));

        assertEquals("User not found", exception.getMessage());

        verify(userRepository, times(1)).findById(userId);
    }

}