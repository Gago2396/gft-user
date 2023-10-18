package com.gfttraining.users.repositories;

import com.gfttraining.users.models.Favorite;
import com.gfttraining.users.models.FavoritePK;
import com.gfttraining.users.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, FavoritePK> {
    Optional<List<Favorite>> findByUser(User user);

    @Transactional
    @Modifying
    @Query("DELETE FROM Favorite f WHERE f.user = :user")
    void deleteByUser(@Param("user") User user);

    @Transactional
    @Modifying
    @Query("DELETE FROM Favorite f WHERE f.product = :product")
    void deleteByProduct(@Param("product") Long product);
}
