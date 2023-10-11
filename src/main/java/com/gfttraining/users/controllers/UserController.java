package com.gfttraining.users.controllers;

import com.gfttraining.users.models.User;
import com.gfttraining.users.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    public ResponseEntity<User> updateUserById(long userId, User updatedUser) {
        return userService.updateUserById(userId, updatedUser);
    }

    public ResponseEntity<List<User>> loadListOfUsers() {
        return userService.loadListOfUsers();
    }
}
