package com.gfttraining.users.controllers;

import com.gfttraining.users.models.Favorite;
import com.gfttraining.users.models.FavoriteRequest;
import com.gfttraining.users.models.FavoriteResponse;
import com.gfttraining.users.models.User;
import com.gfttraining.users.services.FavoriteService;
import com.gfttraining.users.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Favorite> addFavorite(@RequestBody FavoriteRequest favoriteReq) {

        User user = userService.getUserById(favoriteReq.getUser())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        Favorite favorite = new Favorite(user, favoriteReq.getProduct());

        return new ResponseEntity<>(
                favoriteService.addFavorite(favorite),
                HttpStatus.OK
        );
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteFavorite(@RequestBody FavoriteRequest favoriteReq) {
        favoriteService.deleteFavorite(favoriteReq.getUser(), favoriteReq.getProduct());
        return ResponseEntity.ok("Favorite deleted successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> searchUserFavorites(@PathVariable long id) {
        try {
            FavoriteResponse response = favoriteService.searchUserFavorites(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFavoriteByProduct(@PathVariable long id) {
        favoriteService.deleteFavoriteByProduct(id);
        return ResponseEntity.ok("Favorites deleted successfully");
    }

}
