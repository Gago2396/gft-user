package com.gfttraining.users.controllers;

import com.gfttraining.users.models.Favorite;
import com.gfttraining.users.models.FavoriteRequest;
import com.gfttraining.users.models.User;
import com.gfttraining.users.services.FavoriteService;
import com.gfttraining.users.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteControllerTest {

    @InjectMocks
    private FavoriteController favoriteController;

    @Mock
    private FavoriteService favoriteService;

    @Mock
    private UserService userService;

    private FavoriteRequest testFavoriteRequest;
    private User testUser;

    @BeforeEach
    void setUp() {
        // Mock User
        testUser = new User();
        testUser.setId(1L);

        // FavoriteRequest
        testFavoriteRequest = new FavoriteRequest();
        testFavoriteRequest.setUser(1L);
        testFavoriteRequest.setProduct("SampleProduct");
    }

    @Test
    @DisplayName("Add Favorite")
    void testAddFavorite() {
        when(userService.getUserById(testFavoriteRequest.getUser())).thenReturn(testUser);
        Favorite favorite = new Favorite(testUser, testFavoriteRequest.getProduct());
        when(favoriteService.addFavorite(favorite)).thenReturn(favorite);

        ResponseEntity<?> responseEntity = favoriteController.addFavorite(testFavoriteRequest);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(favorite, responseEntity.getBody());
        verify(userService, times(1)).getUserById(testFavoriteRequest.getUser());
        verify(favoriteService, times(1)).addFavorite(favorite);
    }

    @Test
    @DisplayName("Delete Favorite")
    void testDeleteFavorite() {
        doNothing().when(favoriteService).deleteFavorite(testFavoriteRequest.getUser(), testFavoriteRequest.getProduct());

        ResponseEntity<String> responseEntity = favoriteController.deleteFavorite(testFavoriteRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Favorite deleted successfully", responseEntity.getBody());
        verify(favoriteService, times(1)).deleteFavorite(testFavoriteRequest.getUser(), testFavoriteRequest.getProduct());
    }

    @Test
    @DisplayName("Search User Favorites")
    void testSearchUserFavorites() {
        long userId = 1L;
        when(favoriteService.searchUserFavorites(userId)).thenReturn(Arrays.asList(testFavoriteRequest.getProduct()));

        ResponseEntity<?> responseEntity = favoriteController.searchUserFavorites(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Arrays.asList(testFavoriteRequest.getProduct()), responseEntity.getBody());
        verify(favoriteService, times(1)).searchUserFavorites(userId);
    }

    @Test
    @DisplayName("Delete Favorite by Product")
    void testDeleteFavoriteByProduct() {
        long productId = 1L;
        doNothing().when(favoriteService).deleteFavoriteByProduct(productId);

        ResponseEntity<String> responseEntity = favoriteController.deleteFavoriteByProduct(productId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Favorite deleted successfully", responseEntity.getBody());
        verify(favoriteService, times(1)).deleteFavoriteByProduct(productId);
    }
}
