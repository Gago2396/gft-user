package com.gfttraining.users.services;

import com.gfttraining.users.models.Country;
import com.gfttraining.users.repositories.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CountryService {
    private final CountryRepository CountryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.CountryRepository = countryRepository;
    }

    public Optional<Country> getCountryByName(String countryName) {
        return CountryRepository.findByName(countryName);
    }
}
