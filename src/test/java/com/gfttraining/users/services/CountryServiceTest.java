package com.gfttraining.users.services;

import com.gfttraining.users.models.Country;
import com.gfttraining.users.repositories.CountryRepository;
import com.gfttraining.users.services.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CountryServiceTest {

    private CountryService countryService;

    @Mock
    private CountryRepository countryRepository;

    @BeforeEach
    void setUp() {
        countryRepository = mock(CountryRepository.class);
        countryService = new CountryService(countryRepository);
    }

    @Test
    @DisplayName("GIVEN a valid Country name WHEN getCountryByName method is called THEN get a Country")
    void testGetCountryByName() {
        String countryName = "Spain";

        Country expectedCountry = new Country(1L, "Spain");

        when(countryRepository.findByName(countryName)).thenReturn(Optional.of(expectedCountry));

        Optional<Country> result = countryService.getCountryByName(countryName);

        assertEquals(Optional.of(expectedCountry), result);
    }
}
