package com.gfttraining.users.services;

import com.gfttraining.users.models.PaymentMethod;
import com.gfttraining.users.models.User;
import com.gfttraining.users.models.UserRequest;
import com.gfttraining.users.repositories.PaymentMethodRepository;
import com.gfttraining.users.repositories.UserRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

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

    public User createUser(UserRequest userRequest) {

        try {
            PaymentMethod paymentMethod = paymentMethodRepository
                    .findByName(userRequest.getPaymentMethod())
                    //ToDo: Control negative if payment does not exist
                    .orElseThrow(ChangeSetPersister.NotFoundException::new);

            User user = new User();
            user.setName(userRequest.getName());
            user.setLastName(userRequest.getLastName());
            user.setAddress(userRequest.getAddress());
            user.setFidelityPoints(userRequest.getFidelityPoints());
            user.setAveragePurchase(userRequest.getAveragePurchase());
            user.setPaymentMethod(paymentMethod);

            return userRepository.save(user);

        } catch (Exception e) {
            return null;
        }
    }

    public User deleteUserById(Long id) {
        return null;
    }

    public User updateUserById(long userId, User updatedUser) {
        return userRepository.findById(userId)
                .map(existingUser -> {
                    Optional.ofNullable(updatedUser.getName()).ifPresent(existingUser::setName);
                    Optional.ofNullable(updatedUser.getLastName()).ifPresent(existingUser::setLastName);
                    Optional.ofNullable(updatedUser.getAddress()).ifPresent(existingUser::setAddress);
                    Optional.ofNullable(updatedUser.getPaymentMethod()).ifPresent(existingUser::setPaymentMethod);
                    Optional.ofNullable(updatedUser.getFidelityPoints()).ifPresent(existingUser::setFidelityPoints);
                    Optional.ofNullable(updatedUser.getAveragePurchase()).ifPresent(existingUser::setAveragePurchase);

                    userRepository.save(existingUser);

                    return userRepository.findById(userId).get();
                })
                .orElse(null);
    }

    public List<User> loadListOfUsers(List<User> userList) {
        return userRepository.saveAll(userList);
    }

    public Optional<User> getUserById(long id) {
        return userRepository.findById(id);
    }

    public Optional<List<User>> getUserByName(String name) {
        return userRepository.findByName(name);
    }
}
