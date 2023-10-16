package com.gfttraining.users.controllers;

import com.gfttraining.users.models.User;
import com.gfttraining.users.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        return new ResponseEntity<>(
            userService.createUser(user),
            HttpStatus.OK
        );

    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUserById(@PathVariable long id, @RequestBody User updatedUser) {
        User updated = userService.updateUserById(id, updatedUser);
        if (updated != null) {
            return ResponseEntity.status(HttpStatus.OK).body(updated);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUserById(@PathVariable long id) {
        User deleted = userService.deleteUserById(id);
        if (deleted != null) {
            return ResponseEntity.status(HttpStatus.OK).body(deleted);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/load")
    public ResponseEntity<List<User>> loadListOfUsers() {
        List<User> users = userService.loadListOfUsers();
        if (users != null && !users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(users);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
