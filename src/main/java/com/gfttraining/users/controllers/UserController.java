package com.gfttraining.users.controllers;

import com.gfttraining.users.exceptions.PaymentMethodNotFoundException;
import com.gfttraining.users.models.UserRequest;

import com.gfttraining.users.exceptions.ErrorResponse;
import com.gfttraining.users.models.User;

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

//    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid UserRequest userRequest) {
        return new ResponseEntity<>(userService.createUser(userRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserById(@PathVariable long id, @RequestBody User updatedUser) {
        User updated = userService.updateUserById(id, updatedUser);
        if (updated != null) {
            return ResponseEntity.status(HttpStatus.OK).body(updated);
        } else {
            ErrorResponse errorResponse = new ErrorResponse("User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }

    @PostMapping("/load")
    public ResponseEntity<?> loadListOfUsers(@RequestBody List<@Valid UserRequest> userRequestList) {
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

