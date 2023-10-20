package com.gfttraining.users.controllers;

import com.gfttraining.users.models.Favorite;
import com.gfttraining.users.models.FavoriteRequest;
import com.gfttraining.users.models.User;
import com.gfttraining.users.services.FavoriteService;
import com.gfttraining.users.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/users/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final UserService userService;

    public FavoriteController(FavoriteService favoriteService, UserService userService) {
        this.favoriteService = favoriteService;
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<?> addFavorite(@RequestBody @Valid FavoriteRequest favoriteReq) {

        User user = userService.getUserById(favoriteReq.getUser());
        Favorite favorite = new Favorite(user, favoriteReq.getProduct());

        return new ResponseEntity<>(favoriteService.addFavorite(favorite), HttpStatus.CREATED);
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteFavorite(@RequestBody FavoriteRequest favoriteReq) {
        userService.getUserById(favoriteReq.getUser());
        favoriteService.deleteFavorite(favoriteReq.getUser(), favoriteReq.getProduct());
        return new ResponseEntity<>("Favorite deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> searchUserFavorites(@PathVariable long id) {
        return new ResponseEntity<>(favoriteService.searchUserFavorites(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFavoriteByProduct(@PathVariable long id) {
        favoriteService.deleteFavoriteByProduct(id);
        return new ResponseEntity<>("Favorite deleted successfully", HttpStatus.OK);
    }

}
