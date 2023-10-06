package com.gfttraining.users.Repositoy;

import com.gfttraining.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByName(String name);
    void loadUsers();

}
