package com.gfttraining.users.controllers;

import com.gfttraining.users.models.PaymentMethod;
import com.gfttraining.users.models.User;
import com.gfttraining.users.models.UserRequest;
import com.gfttraining.users.services.FavoriteService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class UserControllerIT {

    @LocalServerPort
    private int port;
    private WebTestClient client;
    @Autowired
    private FavoriteService favoriteService;

    public UserControllerIT() {
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
    1.- Create User.
    2.- Create User. - USER Null
    3.- Create User. - USER Not found
    4.- Get All Users
    5.- Get User By ID
    6.- Get User By Name
    7.- Load List of Users
    8.- Update User By ID
    9.- Delete User By ID
    10.- Get User By ID - Not found
    =========================
*/

    @Test
    @DisplayName("Create User Integration Test")
    @Order(1)
    void createUserTest() {
        UserRequest userRequest = new UserRequest(
                "John",
                "Doe",
                "123 Main St",
                "City",
                "Province",
                12345,
                "Spain",
                "PayPal",
                100,
                75.0
        );

        client.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo("John")
                .jsonPath("$.lastName").isEqualTo("Doe")
                .jsonPath("$.address.street").isEqualTo("123 Main St")
                .jsonPath("$.address.city").isEqualTo("City")
                .jsonPath("$.address.province").isEqualTo("Province")
                .jsonPath("$.address.postalCode").isEqualTo(12345)
                .jsonPath("$.address.country.name").isEqualTo("Spain")
                .jsonPath("$.paymentMethod.name").isEqualTo("PayPal")
                .jsonPath("$.fidelityPoints").isEqualTo(100)
                .jsonPath("$.averagePurchase").isEqualTo(75.0);
    }

    @Test
    @DisplayName("Create User IT with Null User - Bad Request")
    @Order(2)
    void postCreateUserTestNull() {
        UserRequest userRequest = new UserRequest(
                null,
                "Doe",
                "123 Main St",
                "City",
                "Province",
                12345,
                "Spain",
                "PayPal",
                100,
                75.0
        );

        client.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userRequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$").isEqualTo("Validation error(s):\n Name is required;");
    }

    /*@Test
    @DisplayName("Create User IT with Invalid Data - Bad Request")
    @Order(3)
    void postCreateUserTestInvalid() {
        UserRequest userRequest = new UserRequest(
                "John",
                "Doe",
                "123 Main St",
                "City",
                "Province",
                12345,
                "Spain",
                "PayPal",
                -1,
                75.0
        );

        client.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userRequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("$").isEqualTo("Validation error(s):\n Fidelity Points must be greater than or equal to 0;");
    }*/

    /*@Test
    @DisplayName("Get List of Users IT")
    @Order(4)
    void getListOfUsersTest() {
        client.get().uri("/users/list")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].name").isEqualTo("John")
                .jsonPath("$[0].lastName").isEqualTo("Doe")
                .jsonPath("$[0].address.street").isEqualTo("123 Main St")
                .jsonPath("$[0].address.city").isEqualTo("City")
                .jsonPath("$[0].address.province").isEqualTo("Province")
                .jsonPath("$[0].address.postalCode").isEqualTo(12345)
                .jsonPath("$[0].address.country.name").isEqualTo("Spain")
                .jsonPath("$[0].paymentMethod.name").isEqualTo("PayPal")
                .jsonPath("$[0].fidelityPoints").isEqualTo(100)
                .jsonPath("$[0].averagePurchase").isEqualTo(75.0);
    }*/

    @Test
    @DisplayName("Get User by ID IT")
    @Order(6)
    void getUserByIdTest() {
        client.get().uri("/users/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("John")
                .jsonPath("$.lastName").isEqualTo("Doe")
                .jsonPath("$.address.id").isEqualTo(1)
                .jsonPath("$.address.street").isEqualTo("Sunset Blvd")
                .jsonPath("$.address.city").isEqualTo("Barcelona")
                .jsonPath("$.address.province").isEqualTo("Catalonia")
                .jsonPath("$.address.postalCode").isEqualTo(12345)
                .jsonPath("$.address.country.id").isEqualTo(1)
                .jsonPath("$.address.country.name").isEqualTo("Spain")
                .jsonPath("$.paymentMethod.id").isEqualTo(1)
                .jsonPath("$.paymentMethod.name").isEqualTo("Credit Card")
                .jsonPath("$.fidelityPoints").isEqualTo(100)
                .jsonPath("$.averagePurchase").isEqualTo(75.50);
    }

}