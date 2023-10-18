package com.gfttraining.users.controllers;

import com.gfttraining.users.exceptions.PaymentMethodNotFoundException;
import com.gfttraining.users.models.UserRequest;
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
    public ResponseEntity<?> createUser(@RequestBody @Valid UserRequest userRequest) {
        try {
            return new ResponseEntity<>(userService.createUser(userRequest), HttpStatus.CREATED);
        } catch (PaymentMethodNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserById(@PathVariable long id, @RequestBody @Valid UserRequest updatedUserRequest) {
        try {
            return new ResponseEntity<>(userService.updateUserById(id, updatedUserRequest), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable long id) {
        try {
            userService.deleteUserById(id);
            return new ResponseEntity<>("User deleted succesfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/load")
    public ResponseEntity<?> loadListOfUsers(@RequestBody @Valid List<UserRequest> userRequestList) {
        try {
            return new ResponseEntity<>(userService.loadListOfUsers(userRequestList), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Ops! Seems like database is down. Try again later!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable long id) {
        try {
            return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<?> getUserByName(@PathVariable String name) {
        try {
            return new ResponseEntity<>(userService.getUserByName(name), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> getListOfUsers() {
        try {
            return new ResponseEntity<>(userService.getListOfUsers(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Ops! Seems like database is down. Try again later!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
