package com.gfttraining.users.repositories;

import com.gfttraining.users.models.Favorite;
import com.gfttraining.users.models.FavoritePK;
import com.gfttraining.users.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, FavoritePK> {
    Optional<List<Favorite>> findByUser(User user);
}
