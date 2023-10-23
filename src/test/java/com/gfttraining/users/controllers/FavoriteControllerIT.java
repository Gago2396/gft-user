package com.gfttraining.users.controllers;

import com.gfttraining.users.models.FavoriteRequest;
import com.gfttraining.users.services.FavoriteService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import javax.annotation.PostConstruct;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class FavoriteControllerIT {

    @LocalServerPort
    private int port;
    private WebTestClient client;
    @Autowired
    private FavoriteService favoriteService;

    public FavoriteControllerIT() {
    }

    //Initiate the web client
    @PostConstruct
    void init() {
        client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:%d".formatted(port))
                .build();
    }

    /*
    ========= ORDER =========
    1.- Create Favorite.
    2.- Create Favorite. - USER Null
    3.- Create Favorite. - USER Not found
    4.- Get Favorite By ID
    5.- Delete Favorite By ID
    6.- Get Favorite By ID - Not found
    =========================
*/
    @Test
    @DisplayName("GIVEN a valid favoriteRequest When a post is made to /users/favorites Then it should be saved in database and return the saved favorite")
    @Order(1)
    void postCreateFavoriteTest() {
        FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 3L);

        client.post().uri("/users/favorites")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(favoriteRequest)
                .exchange()

                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.user.id").isEqualTo(1)
                .jsonPath("$.user.name").isEqualTo("John")
                .jsonPath("$.user.lastName").isEqualTo("Doe")
                .jsonPath("$.user.address.id").isEqualTo(1)
                .jsonPath("$.user.address.street").isEqualTo("Sunset Blvd")
                .jsonPath("$.user.address.city").isEqualTo("Barcelona")
                .jsonPath("$.user.address.province").isEqualTo("Catalonia")
                .jsonPath("$.user.address.postalCode").isEqualTo(12345)
                .jsonPath("$.user.address.country.id").isEqualTo(1)
                .jsonPath("$.user.address.country.name").isEqualTo("Spain")
                .jsonPath("$.user.paymentMethod.id").isEqualTo(1)
                .jsonPath("$.user.paymentMethod.name").isEqualTo("Credit Card")
                .jsonPath("$.user.fidelityPoints").isEqualTo(100)
                .jsonPath("$.user.averagePurchase").isEqualTo(75.50)
                .jsonPath("$.product").isEqualTo(3);
    }

    @Test
    @DisplayName("GIVEN a favoriteRequest with null User WHEN a post is made to /users/favorites THEN it should return Bad Request")
    @Order(2)
    void postCreateFavoriteTestNull() {
        FavoriteRequest favoriteRequest = new FavoriteRequest(null, 3L);

        client.post().uri("/users/favorites")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(favoriteRequest)
                .exchange()

                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$").isEqualTo("Validation error(s):\n" +
                        " User is required;");
    }

    @Test
    @DisplayName("GIVEN a favoriteRequest with not existing User WHEN a post is made to /users/favorites THEN it should return Not Found")
    @Order(3)
    void postCreateFavoriteTestInvalid() {
        FavoriteRequest favoriteRequest = new FavoriteRequest(134L, 3L);

        client.post().uri("/users/favorites")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(favoriteRequest)
                .exchange()

                .expectStatus().isNotFound()
                .expectBody().jsonPath("$").isEqualTo("User not found");
    }

    @Test
    @DisplayName("GIVEN a userId WHEN a get is made to /users/favorites THEN it should return 200 and FavoriteDTO")
    @Order(4)
    void getFavoriteByIdTest() {
        client.get().uri("/users/favorites/1")
                .exchange()

                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.user.id").isEqualTo(1)
                .jsonPath("$.user.name").isEqualTo("John")
                .jsonPath("$.user.lastName").isEqualTo("Doe")
                .jsonPath("$.user.address.id").isEqualTo(1)
                .jsonPath("$.user.address.street").isEqualTo("Sunset Blvd")
                .jsonPath("$.user.address.city").isEqualTo("Barcelona")
                .jsonPath("$.user.address.province").isEqualTo("Catalonia")
                .jsonPath("$.user.address.postalCode").isEqualTo(12345)
                .jsonPath("$.user.address.country.id").isEqualTo(1)
                .jsonPath("$.user.address.country.name").isEqualTo("Spain")
                .jsonPath("$.user.paymentMethod.id").isEqualTo(1)
                .jsonPath("$.user.paymentMethod.name").isEqualTo("Credit Card")
                .jsonPath("$.user.fidelityPoints").isEqualTo(100)
                .jsonPath("$.user.averagePurchase").isEqualTo(75.50)
                .jsonPath("$.favorites[0]").isEqualTo(3);
    }

    @Test
    @DisplayName("GIVEN a valid favoriteRequest WHEN a delete is made to /users/favorites THEN it should delete favorite and return 200")
    @Order(5)
    void deleteFavoriteTest() {
        FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 3L);

        client.method(HttpMethod.DELETE).uri("/users/favorites/")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(favoriteRequest)
                .exchange()

                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isEqualTo("Favorite deleted successfully");
    }

    @Test
    @DisplayName("GIVEN a userId WHEN a get is made to /users/favorites THEN it should return 200 and FavoriteDTO")
    @Order(6)
    void getFavoriteByIdTestInvalid() {
        client.get().uri("/users/favorites/1")
                .exchange()

                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.user.id").isEqualTo(1)
                .jsonPath("$.user.name").isEqualTo("John")
                .jsonPath("$.user.lastName").isEqualTo("Doe")
                .jsonPath("$.user.address.id").isEqualTo(1)
                .jsonPath("$.user.address.street").isEqualTo("Sunset Blvd")
                .jsonPath("$.user.address.city").isEqualTo("Barcelona")
                .jsonPath("$.user.address.province").isEqualTo("Catalonia")
                .jsonPath("$.user.address.postalCode").isEqualTo(12345)
                .jsonPath("$.user.address.country.id").isEqualTo(1)
                .jsonPath("$.user.address.country.name").isEqualTo("Spain")
                .jsonPath("$.user.paymentMethod.id").isEqualTo(1)
                .jsonPath("$.user.paymentMethod.name").isEqualTo("Credit Card")
                .jsonPath("$.user.fidelityPoints").isEqualTo(100)
                .jsonPath("$.user.averagePurchase").isEqualTo(75.50)
                .jsonPath("$.favorites").isEmpty();
    }
}
