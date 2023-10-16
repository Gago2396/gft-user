package com.gfttraining.users.services;

import com.gfttraining.users.models.User;
import com.gfttraining.users.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        if (isValidUser(user)) {
            try {
                return userRepository.save(user);
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public User deleteUserById(Long id){
        return null;
    }

    private boolean isValidUser(User user) {
        return user == null || user.getName() == null || user.getName().isEmpty() ||
                user.getLastName() == null || user.getLastName().isEmpty() ||
                user.getAddress() == null || user.getAddress().isEmpty() ||
                user.getPaymentMethod() == null;
    }

    public User updateUserById(long id, User updatedUser) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }

        updatedUser.setId(id);

        return userRepository.save(updatedUser);
    }
    public List<User> loadListOfUsers() {
        // ToDo: Implement logic to load and return the list of users
        return null;
    }

    public Optional<User> getUserById(long id) {
        return userRepository.findById(id);
    }
}
