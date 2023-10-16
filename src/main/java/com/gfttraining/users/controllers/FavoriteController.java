package com.gfttraining.users.controllers;

import com.gfttraining.users.models.Favorite;
import com.gfttraining.users.models.FavoriteRequest;
import com.gfttraining.users.models.User;
import com.gfttraining.users.services.FavoriteService;
import com.gfttraining.users.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/users/favorite")
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

        Favorite favorite = new Favorite(user, favoriteReq.getProductId());

        return new ResponseEntity<>(
                favoriteService.addFavorite(favorite),
                HttpStatus.OK
        );
    }

}
