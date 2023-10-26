package com.gfttraining.users.controllers;

import com.gfttraining.users.models.Favorite;
import com.gfttraining.users.models.FavoriteDTO;
import com.gfttraining.users.models.FavoriteRequest;
import com.gfttraining.users.models.User;
import com.gfttraining.users.services.FavoriteService;
import com.gfttraining.users.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteControllerTest {

    @Mock
    private FavoriteService favoriteService;

    @Mock
    private UserService userService;

    private FavoriteController favoriteController;

    @BeforeEach
    void setUp() {
        favoriteController = new FavoriteController(favoriteService, userService);
    }

    @Test
    @DisplayName("GIVEN a valid FavoriteRequest WHEN addFavorite method is called THEN return a Favorite and CREATED")
    public void testAddFavoriteSuccess() {
        // GIVEN
        FavoriteRequest favoriteRequest = new FavoriteRequest();
        favoriteRequest.setUser(1L);
        favoriteRequest.setProduct(1L);

        User mockUser = new User();
        when(userService.getUserById(favoriteRequest.getUser())).thenReturn(mockUser);

        when(favoriteService.addFavorite(any(Favorite.class))).thenReturn(new Favorite());

        // WHEN
        ResponseEntity<?> response = favoriteController.addFavorite(favoriteRequest);

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(favoriteService, times(1)).addFavorite(any(Favorite.class));
    }

    @Test
    @DisplayName("GIVEN a non-existent user WHEN addFavorite method is called THEN throw NoSuchElementException")
    void testAddFavoriteInvalid() {
        // GIVEN
        FavoriteRequest requestWithNonExistentUser = new FavoriteRequest();
        requestWithNonExistentUser.setUser(999L);

        // WHEN
        when(userService.getUserById(requestWithNonExistentUser.getUser()))
                .thenThrow(new NoSuchElementException("User not found"));

        // THEN
        assertThrows(NoSuchElementException.class, () -> {
            favoriteController.addFavorite(requestWithNonExistentUser);
        });
    }


    @Test
    @DisplayName("GIVEN a valid FavoriteRequest WHEN deleteFavorite method is called THEN return OK and delete the favorite")
    public void testDeleteFavoriteSuccess() {
        // GIVEN
        FavoriteRequest favoriteRequest = new FavoriteRequest();
        favoriteRequest.setUser(1L);
        favoriteRequest.setProduct(1L);

        User mockUser = new User();
        when(userService.getUserById(favoriteRequest.getUser())).thenReturn(mockUser);

        // WHEN
        ResponseEntity<String> response = favoriteController.deleteFavorite(favoriteRequest);

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(favoriteService, times(1)).deleteFavorite(eq(1L), eq(1L));
    }

    @Test
    @DisplayName("GIVEN a non-existent user WHEN deleteFavorite method is called THEN throw NoSuchElementException")
    void testDeleteFavoriteInvalid() {
        // GIVEN
        FavoriteRequest requestWithNonExistentUser = new FavoriteRequest();
        requestWithNonExistentUser.setUser(999L);

        // WHEN
        when(userService.getUserById(requestWithNonExistentUser.getUser()))
                .thenThrow(new NoSuchElementException("User not found"));

        // THEN
        assertThrows(NoSuchElementException.class, () -> {
            favoriteController.deleteFavorite(requestWithNonExistentUser);
        });
    }

    @Test
    @DisplayName("GIVEN a valid user ID WHEN searchUserFavorites method is called THEN return OK and a list of favorites")
    public void testSearchUserFavoritesSuccess() {
        // GIVEN
        long userId = 1L;
        User user = new User();

        when(userService.getUserById(userId)).thenReturn(user);
        when(favoriteService.searchUserFavorites(user)).thenReturn(new FavoriteDTO());

        // WHEN
        ResponseEntity<?> response = favoriteController.searchUserFavorites(userId);

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isInstanceOf(FavoriteDTO.class);
    }

    @Test
    @DisplayName("GIVEN a non-existent user WHEN searchUserFavorites method is called THEN throw NoSuchElementException")
    void testSearchUserFavoritesInvalid() {
        // GIVEN
        long userId = 999L;

        // WHEN
        when(userService.getUserById(userId))
                .thenThrow(new NoSuchElementException("User not found"));

        // THEN
        assertThrows(NoSuchElementException.class, () -> {
            favoriteController.searchUserFavorites(userId);
        });
    }
}