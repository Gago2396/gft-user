package com.gfttraining.users.repositories;

import com.gfttraining.users.models.PaymentMethod;
import com.gfttraining.users.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    User findByName(String name);

}
