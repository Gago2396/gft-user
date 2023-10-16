package com.gfttraining.users.repositories;

import com.gfttraining.users.models.Favorite;
import com.gfttraining.users.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

}
