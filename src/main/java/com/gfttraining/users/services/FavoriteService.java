package com.gfttraining.users.services;

import com.gfttraining.users.models.Favorite;
import com.gfttraining.users.models.FavoritePK;
import com.gfttraining.users.models.FavoriteDTO;
import com.gfttraining.users.models.User;
import com.gfttraining.users.repositories.FavoriteRepository;
import com.gfttraining.users.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    private final UserRepository userRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, UserRepository userRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
    }

    public Favorite addFavorite(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    public void deleteFavorite(Long user, Long product) {
        FavoritePK id = new FavoritePK(user, product);
        favoriteRepository.deleteById(id);
    }

    public FavoriteDTO searchUserFavorites(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        List<Favorite> favList = favoriteRepository.findByUser(user)
                .orElseThrow(() -> new NoSuchElementException("Favorites not found"));

        List<Long> productIds = favList.stream()
                .map(Favorite::getProduct)
                .collect(Collectors.toList());

        return new FavoriteDTO(user, productIds);
    }

    public void deleteFavoriteByProduct(Long productId) {
//        ToDo: Add NOT_FOUND response when microservices are connected
        favoriteRepository.deleteByProduct(productId);
    }
}
