package com.gfttraining.users.initdata;

import com.gfttraining.users.models.Favorite;
import com.gfttraining.users.models.PaymentMethod;
import com.gfttraining.users.models.User;
import com.gfttraining.users.repositories.FavoriteRepository;
import com.gfttraining.users.repositories.PaymentMethodRepository;
import com.gfttraining.users.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PaymentMethodRepository paymentMethodRepository;

    private final FavoriteRepository favoriteRepository;

    public DataInitializer(UserRepository userRepository, PaymentMethodRepository paymentMethodRepository, FavoriteRepository favoriteRepository) {
        this.userRepository = userRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.favoriteRepository = favoriteRepository;
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

        List<User> users = new ArrayList<>();
        users.add(new User(1L, "John", "Doe", "1234 Elm St", paymentMethod1, 100, 75.50));
        users.add(new User(2L, "Jane", "Smith", "5678 Oak St", paymentMethod2, 150, 100.20));
        users.add(new User(3L, "Michael", "Johnson", "789 Main St", paymentMethod1, 120, 90.75));
        users.add(new User(4L, "Emily", "Davis", "456 Pine St", paymentMethod2, 200, 120.30));
        users.add(new User(5L, "David", "Anderson", "1010 Maple St", paymentMethod3, 80, 60.00));
        users.add(new User(6L, "Sarah", "Johnson", "2222 Oak St", paymentMethod3, 90, 80.00));
        users.add(new User(7L, "Mark", "Davis", "3333 Elm St", paymentMethod3, 120, 95.50));
        users.add(new User(8L, "Anna", "Smith", "4444 Pine St", paymentMethod1, 180, 150.75));
        users.add(new User(9L, "Michael", "Wilson", "5555 Maple St", paymentMethod2, 140, 110.20));
        users.add(new User(10L, "Jennifer", "Brown", "6666 Cedar St", paymentMethod1, 160, 120.00));

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
