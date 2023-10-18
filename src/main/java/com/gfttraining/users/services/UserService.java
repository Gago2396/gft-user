package com.gfttraining.users.services;

import com.gfttraining.users.exceptions.PaymentMethodNotFoundException;
import com.gfttraining.users.models.Favorite;
import com.gfttraining.users.models.PaymentMethod;
import com.gfttraining.users.models.User;
import com.gfttraining.users.models.UserRequest;
import com.gfttraining.users.repositories.PaymentMethodRepository;
import com.gfttraining.users.repositories.UserRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PaymentMethodRepository paymentMethodRepository;

    private final PaymentMethodService paymentMethodService;

    public UserService(UserRepository userRepository, PaymentMethodRepository paymentMethodRepository, PaymentMethodService paymentMethodService) {
        this.userRepository = userRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentMethodService = paymentMethodService;
    }

    public PaymentMethod findPaymentMethod(String paymentMethodName) {
        try {
            return paymentMethodService
                    .getPaymentMethodByName(paymentMethodName)
                    .orElseThrow(() -> new PaymentMethodNotFoundException("PaymentMethod not found"));
        } catch (PaymentMethodNotFoundException e) {
            throw e;
        }
    }



    public User createUser(UserRequest userRequest) {
        User user = parseUser(userRequest);
        return userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public User updateUserById(long userId, UserRequest updatedUserRequest) {

        userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        User user = parseUser(updatedUserRequest);
        user.setId(userId);

        return userRepository.save(user);
    }

    public User parseUser(UserRequest userRequest){

        PaymentMethod paymentMethod = findPaymentMethod(userRequest.getPaymentMethod());

        User user = new User();
        user.setName(userRequest.getName());
        user.setLastName(userRequest.getLastName());
        user.setAddress(userRequest.getAddress());
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

    public Optional<User> getUserById(long id) {
        return userRepository.findById(id);
    }

    public Optional<List<User>> getUserByName(String name) {
        return userRepository.findByName(name);
    }

    public Favorite addFavorite(Favorite favorite) {
        return null;
    }

    public List<User> getListOfUsers() {
        return userRepository.findAll();
    }
}
