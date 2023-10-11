package com.gfttraining.users.services;

import com.gfttraining.users.models.User;
import com.gfttraining.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<User> createUser(User user) {
        if (isValidUser(user)) {
            try {
                User savedUser = userRepository.save(user);
                return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isValidUser(User user) {
        return user == null || user.getName() == null || user.getName().isEmpty() ||
                user.getLastName() == null || user.getLastName().isEmpty() ||
                user.getAddress() == null || user.getAddress().isEmpty() ||
                user.getPaymentMethod() == null;
    }

    public ResponseEntity<User> updateUserById(long userId, User updatedUser) {
        return null;
    }

    public ResponseEntity<List<User>> loadListOfUsers() {
        return null;
    }
}
