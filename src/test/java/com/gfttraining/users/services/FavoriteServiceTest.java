package com.gfttraining.users.services;

import com.gfttraining.users.models.Favorite;
import com.gfttraining.users.models.FavoriteDTO;
import com.gfttraining.users.models.FavoritePK;
import com.gfttraining.users.models.User;
import com.gfttraining.users.repositories.FavoriteRepository;
import com.gfttraining.users.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private UserRepository userRepository;

    private FavoriteService favoriteService;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(favoriteRepository, userRepository);
    }

    @Test
    @DisplayName("GIVEN a favorite WHEN addFavorite method is called THEN return the favorite")
    void addFavorite() {
        // GIVEN
        Favorite favorite = new Favorite();
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(favorite);

        // WHEN
        Favorite result = favoriteService.addFavorite(favorite);

        // THEN
        assertThat(result).isNotNull();
        verify(favoriteRepository, times(1)).save(any(Favorite.class));
    }

    @Test
    @DisplayName("GIVEN user and product IDs WHEN deleteFavorite method is called THEN delete the favorite")
    void deleteFavorite() {
        // GIVEN
        Long userId = 1L;
        Long productId = 1L;
        FavoritePK id = new FavoritePK(userId, productId);

        // WHEN
        favoriteService.deleteFavorite(userId, productId);

        // THEN
        verify(favoriteRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("GIVEN a user with favorites WHEN searchUserFavorites method is called THEN return a DTO with user and favorites")
    void searchUserFavorites() {
        // GIVEN
        User user = new User();
        user.setId(1L);
        List<Favorite> favorites = Arrays.asList(new Favorite(user, 1L), new Favorite(user, 2L));

        when(favoriteRepository.findByUser(user)).thenReturn(favorites);

        // WHEN
        FavoriteDTO result = favoriteService.searchUserFavorites(user);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getUser().getId()).isEqualTo(user.getId());
        assertThat(result.getFavorites().size()).isEqualTo(favorites.size());
        verify(favoriteRepository, times(1)).findByUser(user);
    }
}
