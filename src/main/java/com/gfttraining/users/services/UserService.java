package com.gfttraining.users.services;

import com.gfttraining.users.models.PaymentMethod;
import com.gfttraining.users.models.User;
import com.gfttraining.users.models.UserRequest;
import com.gfttraining.users.repositories.PaymentMethodRepository;
import com.gfttraining.users.repositories.UserRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PaymentMethodRepository paymentMethodRepository;

    public UserService(UserRepository userRepository, PaymentMethodRepository paymentMethodRepository) {
        this.userRepository = userRepository;
        this.paymentMethodRepository = paymentMethodRepository;
    }

    public PaymentMethod findPaymentMethod(String paymentMethodName) {
        try {
            PaymentMethod paymentMethod = paymentMethodRepository
                    .findByName(paymentMethodName)
                    //ToDo: Control negative if payment does not exist
                    .orElseThrow(ChangeSetPersister.NotFoundException::new);
            return paymentMethod;
        } catch (Exception e) {
            return null;
        }
    }

    public User createUser(UserRequest userRequest) {

        PaymentMethod paymentMethod = findPaymentMethod(userRequest.getPaymentMethod());

        User user = new User();
        user.setName(userRequest.getName());
        user.setLastName(userRequest.getLastName());
        user.setAddress(userRequest.getAddress());
        user.setFidelityPoints(userRequest.getFidelityPoints());
        user.setAveragePurchase(userRequest.getAveragePurchase());
        user.setPaymentMethod(paymentMethod);

        return userRepository.save(user);

    }

    public User deleteUserById(Long id) {
        return null;
    }

    public User updateUserById(long userId, UserRequest updatedUserRequest) {

        PaymentMethod paymentMethod = findPaymentMethod(updatedUserRequest.getPaymentMethod());

        return userRepository.findById(userId)
                .map(existingUser -> {
                    Optional.ofNullable(updatedUserRequest.getName()).ifPresent(existingUser::setName);
                    Optional.ofNullable(updatedUserRequest.getLastName()).ifPresent(existingUser::setLastName);
                    Optional.ofNullable(updatedUserRequest.getAddress()).ifPresent(existingUser::setAddress);
                    Optional.ofNullable(paymentMethod).ifPresent(existingUser::setPaymentMethod);
                    Optional.ofNullable(updatedUserRequest.getFidelityPoints()).ifPresent(existingUser::setFidelityPoints);
                    Optional.ofNullable(updatedUserRequest.getAveragePurchase()).ifPresent(existingUser::setAveragePurchase);

                    userRepository.save(existingUser);

                    return userRepository.findById(userId).get();
                })
                .orElse(null);
    }

    public List<User> loadListOfUsers(List<UserRequest> userRequestList) {

        List<User> usersToLoad = new ArrayList<>();

        //ToDo: Manejar userRequest nulo
        for (UserRequest userRequest : userRequestList) {
            //ToDo: Manejar paymentMethod nulo
            PaymentMethod paymentMethod = findPaymentMethod(userRequest.getPaymentMethod());

            User user = new User();
            user.setName(userRequest.getName());
            user.setLastName(userRequest.getLastName());
            user.setAddress(userRequest.getAddress());
            user.setFidelityPoints(userRequest.getFidelityPoints());
            user.setAveragePurchase(userRequest.getAveragePurchase());
            user.setPaymentMethod(paymentMethod);

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
}
