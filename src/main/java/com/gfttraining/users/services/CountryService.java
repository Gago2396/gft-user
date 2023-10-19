package com.gfttraining.users.services;

import com.gfttraining.users.models.Country;
import com.gfttraining.users.repositories.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CountryService {
    private final CountryRepository CountryRepository;

    public CountryService(CountryRepository paymentMethodRepository) {
        this.CountryRepository = paymentMethodRepository;
    }

    public Optional<Country> getCountryByName(String paymentMethodName) {
        return CountryRepository.findByName(paymentMethodName);
    }
}
