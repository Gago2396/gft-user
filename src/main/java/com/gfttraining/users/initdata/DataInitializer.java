package com.gfttraining.users.initdata;

import com.gfttraining.users.models.*;
import com.gfttraining.users.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PaymentMethodRepository paymentMethodRepository;

    private final FavoriteRepository favoriteRepository;

    private final CountryRepository countryRepository;

    private final AddressRepository addressRepository;

    public DataInitializer(UserRepository userRepository, PaymentMethodRepository paymentMethodRepository, FavoriteRepository favoriteRepository, CountryRepository countryRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.favoriteRepository = favoriteRepository;
        this.countryRepository = countryRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public void run(String... args) {
        initializeData();
    }

    private void initializeData() {
        PaymentMethod paymentMethod1 = new PaymentMethod(1L, "Credit Card");
        PaymentMethod paymentMethod2 = new PaymentMethod(2L, "PayPal");
        PaymentMethod paymentMethod3 = new PaymentMethod(3L, "Stripe");

        paymentMethodRepository.save(paymentMethod1);
        paymentMethodRepository.save(paymentMethod2);
        paymentMethodRepository.save(paymentMethod3);

        List<Country> countries = new ArrayList<>();
        countries.add(new Country(1L, "Spain"));
        countries.add(new Country(2L, "Estonia"));
        countries.add(new Country(3L, "Finland"));
        countries.add(new Country(4L, "France"));
        countries.add(new Country(5L, "Italy"));
        countries.add(new Country(6L, "Portugal"));
        countries.add(new Country(7L, "Greece"));

        countryRepository.saveAll(countries);

        List<Address> addresses = new ArrayList<>();
        addresses.add(new Address(1L, "Sunset Blvd", "Barcelona", "Catalonia", 12345, countries.get(0)));
        addresses.add(new Address(2L, "Green Valley Rd", "Tallinn", "Harju County", 56789, countries.get(1)));
        addresses.add(new Address(3L, "Northern Lights Ave", "Helsinki", "Uusimaa", 10111, countries.get(2)));
        addresses.add(new Address(4L, "Eiffel Tower Street", "Paris", "Ile-de-France", 31415, countries.get(3)));
        addresses.add(new Address(5L, "Roman Forum Lane", "Rome", "Lazio", 98765, countries.get(4)));
        addresses.add(new Address(6L, "Port Wine Street", "Lisbon", "Lisbon District", 54321, countries.get(5)));
        addresses.add(new Address(7L, "Olive Grove Street", "Athens", "Attica", 67890, countries.get(6)));
        addresses.add(new Address(8L, "Sagrada Familia Blvd", "Barcelona", "Catalonia", 24680, countries.get(0)));
        addresses.add(new Address(9L, "Estonian Forest Rd", "Tallinn", "Harju County", 13579, countries.get(1)));
        addresses.add(new Address(10L, "Lakeside Drive", "Helsinki", "Uusimaa", 11223, countries.get(2)));

        addressRepository.saveAll(addresses);

        List<User> users = new ArrayList<>();
        users.add(new User(1L, "John", "Doe", addresses.get(0), paymentMethod1, 100, 75.50));
        users.add(new User(2L, "Jane", "Smith", addresses.get(1), paymentMethod2, 150, 100.20));
        users.add(new User(3L, "Michael", "Johnson", addresses.get(2), paymentMethod1, 120, 90.75));
        users.add(new User(4L, "Emily", "Davis", addresses.get(3), paymentMethod2, 200, 120.30));
        users.add(new User(5L, "David", "Anderson", addresses.get(4), paymentMethod3, 80, 60.00));
        users.add(new User(6L, "Sarah", "Johnson", addresses.get(5), paymentMethod3, 90, 80.00));
        users.add(new User(7L, "Mark", "Davis", addresses.get(6), paymentMethod3, 120, 95.50));
        users.add(new User(8L, "Anna", "Smith", addresses.get(7), paymentMethod1, 180, 150.75));
        users.add(new User(9L, "Michael", "Wilson", addresses.get(8), paymentMethod2, 140, 110.20));
        users.add(new User(10L, "Jennifer", "Brown", addresses.get(9), paymentMethod1, 160, 120.00));

        userRepository.saveAll(users);

        List<Favorite> favorites = new ArrayList<>();
        favorites.add(new Favorite(users.get(2), 3L));
        favorites.add(new Favorite(users.get(2), 4L));
        favorites.add(new Favorite(users.get(5), 3L));
        favorites.add(new Favorite(users.get(3), 2L));
        favorites.add(new Favorite(users.get(1), 1L));
        favorites.add(new Favorite(users.get(1), 1L));

        favoriteRepository.saveAll(favorites);
    }
}
