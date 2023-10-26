package com.gfttraining.users.services;

import com.gfttraining.users.exceptions.CartConnectionRefusedException;
import com.gfttraining.users.exceptions.CartResponseFailedException;
import com.gfttraining.users.exceptions.CountryNotFoundException;
import com.gfttraining.users.exceptions.PaymentMethodNotFoundException;
import com.gfttraining.users.models.*;
import com.gfttraining.users.repositories.FavoriteRepository;
import com.gfttraining.users.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CountryService countryService;
    private final AddressService addressService;
    private final PaymentMethodService paymentMethodService;
    private final FavoriteRepository favoriteRepository;
    private final CartService cartService;

    public UserService(UserRepository userRepository, CountryService countryService, AddressService addressService, PaymentMethodService paymentMethodService, FavoriteRepository favoriteRepository, CartService cartService) {
        this.userRepository = userRepository;
        this.countryService = countryService;
        this.addressService = addressService;
        this.paymentMethodService = paymentMethodService;
        this.favoriteRepository = favoriteRepository;
        this.cartService = cartService;
    }

    public User createUser(UserRequest userRequest) {
        User user = parseUser(userRequest);
        return userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        favoriteRepository.deleteByUser(user);
        userRepository.deleteById(id);
    }

    public User updateUserById(long userId, UserRequest updatedUserRequest) {

        userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        User user = parseUser(updatedUserRequest);
        user.setId(userId);

        return userRepository.save(user);
    }

    public User updateUserFidelityPoints(Long userId) throws CartResponseFailedException, CartConnectionRefusedException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        int fidelityPoints = cartService.getFidelityPoints(userId);

        user.setFidelityPoints(fidelityPoints);

        return userRepository.save(user);
    }

    public User parseUser(UserRequest userRequest) {

        PaymentMethod paymentMethod = paymentMethodService.getPaymentMethodByName(userRequest.getPaymentMethod())
                .orElseThrow(() -> new PaymentMethodNotFoundException("Payment method not valid"));

        Country country = countryService.getCountryByName(userRequest.getCountry())
                .orElseThrow(() -> new CountryNotFoundException("Country not found"));

        AddressRequest addressRequest = new AddressRequest(userRequest.getStreet(), userRequest.getCity(), userRequest.getProvince(), userRequest.getPostalCode(), country);

        Address address = addressService.parseAddress(addressRequest);
        addressService.addAddress(address);

        User user = new User();
        user.setName(userRequest.getName());
        user.setLastName(userRequest.getLastName());
        user.setAddress(address);
        user.setFidelityPoints(userRequest.getFidelityPoints());
        user.setAveragePurchase(userRequest.getAveragePurchase());
        user.setPaymentMethod(paymentMethod);

        return user;
    }

    public List<User> loadListOfUsers(List<UserRequest> userRequestList) {

        List<User> usersToLoad = new ArrayList<>();

        for (UserRequest userRequest : userRequestList) {
            User user = parseUser(userRequest);
            usersToLoad.add(user);
        }

        return userRepository.saveAll(usersToLoad);
    }

    public User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    public List<User> getUserByName(String name) {
        return userRepository.findByName(name)
                .orElse(Collections.emptyList());
    }

    public List<User> getListOfUsers() {
        return userRepository.findAll();
    }
}
