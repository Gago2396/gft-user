package com.gfttraining.users.repositories;

import com.gfttraining.users.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
