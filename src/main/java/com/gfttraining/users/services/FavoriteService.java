package com.gfttraining.users.services;

import com.gfttraining.users.models.Favorite;
import com.gfttraining.users.models.FavoritePK;
import com.gfttraining.users.repositories.FavoriteRepository;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public Favorite addFavorite(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    public void deleteFavorite(Long user, Long product) {
        FavoritePK id = new FavoritePK(user, product);
        favoriteRepository.deleteById(id);
    }
}
