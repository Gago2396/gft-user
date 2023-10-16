package com.gfttraining.users.controllers;

import com.gfttraining.users.exceptions.ErrorResponse;
import com.gfttraining.users.models.User;
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

//    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        return new ResponseEntity<>(
            userService.createUser(user),
            HttpStatus.OK
        );

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
    public ResponseEntity<User> deleteUserById(@PathVariable long id) {
        User deleted = userService.deleteUserById(id);
        if (deleted != null) {
            return ResponseEntity.status(HttpStatus.OK).body(deleted);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/load")
    public ResponseEntity<List<User>> loadListOfUsers(@RequestBody List<User> userList) {
        List<User> savedUsers = userService.loadListOfUsers(userList);
        if (savedUsers != null && !savedUsers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUsers);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id) {
        Optional<User> user = userService.getUserById(id);

        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<List<User>> getUserByName(@PathVariable String name) {
        Optional<List<User>> user = userService.getUserByName(name);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
