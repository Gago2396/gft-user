package com.gfttraining.users.controllers;

import com.gfttraining.users.models.Favorite;
import com.gfttraining.users.models.User;
import com.gfttraining.users.models.UserRequest;
import com.gfttraining.users.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid UserRequest userRequest) {
        return new ResponseEntity<>(
            userService.createUser(userRequest),
            HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUserById(@PathVariable long id, @RequestBody UserRequest updatedUserRequest) {
        User updated = userService.updateUserById(id, updatedUserRequest);
        if (updated != null) {
            return ResponseEntity.status(HttpStatus.OK).body(updated);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable long id) {
        userService.deleteUserById(id);
        //if (deleted != null) {
        //    return ResponseEntity.status(HttpStatus.OK).body(deleted);
        //} else {
        //    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        //}
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/load")
    public ResponseEntity<List<User>> loadListOfUsers(@RequestBody List<UserRequest> userRequestList) {
        List<User> savedUsers = userService.loadListOfUsers(userRequestList);
        if (savedUsers != null && !savedUsers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUsers);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id) {
        Optional<User> user = userService.getUserById(id);

        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<List<User>> getUserByName(@PathVariable String name) {
        Optional<List<User>> user = userService.getUserByName(name);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
