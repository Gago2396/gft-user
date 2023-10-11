package com.gfttraining.users.controllers;

import com.gfttraining.users.models.User;
import com.gfttraining.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/users/{id}")
    public ResponseEntity<User> deleteUserById(@PathVariable Long id) {
        return userService.deleteUserById(id);
    }

    public ResponseEntity<User> updateUserById(long userId, User updatedUser) {
        return userService.updateUserById(userId, updatedUser);
    }

    public ResponseEntity<List<User>> loadListOfUsers() {
        return userService.loadListOfUsers();
    }
}
