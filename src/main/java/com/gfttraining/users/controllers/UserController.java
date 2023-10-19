package com.gfttraining.users.controllers;

import com.gfttraining.users.exceptions.PaymentMethodNotFoundException;
import com.gfttraining.users.models.UserRequest;
import com.gfttraining.users.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid UserRequest userRequest) {
            return new ResponseEntity<>(userService.createUser(userRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserById(@PathVariable long id, @RequestBody @Valid UserRequest updatedUserRequest) {
            return new ResponseEntity<>(userService.updateUserById(id, updatedUserRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable long id) {
            userService.deleteUserById(id);
            return new ResponseEntity<>("User deleted succesfully", HttpStatus.OK);
    }

    @PostMapping("/load")
    public ResponseEntity<?> loadListOfUsers(@RequestBody @Valid List<UserRequest> userRequestList) {
            return new ResponseEntity<>(userService.loadListOfUsers(userRequestList), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<?> getUserByName(@PathVariable String name) {
        return new ResponseEntity<>(userService.getUserByName(name), HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getListOfUsers() {
            return new ResponseEntity<>(userService.getListOfUsers(), HttpStatus.OK);
        }
    }

