package com.gfttraining.users.controllers;

import com.gfttraining.users.models.PaymentMethod;
import com.gfttraining.users.models.User;
import com.gfttraining.users.models.UserRequest;
import com.gfttraining.users.services.FavoriteService;
import net.minidev.json.JSONObject;
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
                "Lennon",
                "123 Main St",
                "Liverpool",
                "Merseyside",
                13456,
                "United Kingdom",
                "PayPal",
                170,
                75.0
        );

        client.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo("John")
                .jsonPath("$.lastName").isEqualTo("Lennon")
                .jsonPath("$.address.street").isEqualTo("123 Main St")
                .jsonPath("$.address.city").isEqualTo("Liverpool")
                .jsonPath("$.address.province").isEqualTo("Merseyside")
                .jsonPath("$.address.postalCode").isEqualTo(13456)
                .jsonPath("$.address.country.name").isEqualTo("Portugal")
                .jsonPath("$.paymentMethod.name").isEqualTo("PayPal")
                .jsonPath("$.fidelityPoints").isEqualTo(170)
                .jsonPath("$.averagePurchase").isEqualTo(75.0);
    }

    @Test
    @DisplayName("Create User IT with Null User - Bad Request")
    @Order(2)
    void postCreateUserTestNull() {
        UserRequest userRequest = new UserRequest(
                "John",
                "Lennon",
                "123 Main St",
                "Liverpool",
                "Merseyside",
                13456,
                "Portugal",
                "PayPal",
                170,
                75.0
        );

        client.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userRequest)
                .exchange()
//                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$").isEqualTo("Validation error(s):\n Name is required;");
    }

    @Test
    @DisplayName("Create User IT with Invalid Data - Bad Request: Name is required")
    @Order(3)
    void postCreateUserTestInvalid() {
        JSONObject userRequest = new JSONObject();
        userRequest.put("lastName", "Lennon");

        JSONObject address = new JSONObject();
        address.put("street", "123 Main St");
        address.put("city", "Liverpool");
        address.put("province", "Merseyside");
        address.put("postalCode", 13456);

        JSONObject country = new JSONObject();
        country.put("name", "United Kingdom");

        address.put("country", country);

        userRequest.put("address", address);
        userRequest.put("paymentMethod", "PayPal");
        userRequest.put("fidelityPoints", 170);
        userRequest.put("averagePurchase", 75.0);

        String userRequestJson = userRequest.toString();

        client.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userRequestJson)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$").isEqualTo("Validation error(s):\n" +
                        " Name is required;");
    }

    @Test
    @DisplayName("Get List of Users IT")
    @Order(4)
    void getListOfUsersTest() {
        client.get().uri("/users/list")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].name").isEqualTo("John")
                .jsonPath("$[0].lastName").isEqualTo("Doe")
                .jsonPath("$[0].address.street").isEqualTo("Sunset Blvd")
                .jsonPath("$[0].address.city").isEqualTo("Barcelona")
                .jsonPath("$[0].address.province").isEqualTo("Catalonia")
                .jsonPath("$[0].address.postalCode").isEqualTo(12345)
                .jsonPath("$[0].address.country.name").isEqualTo("Spain")
                .jsonPath("$[0].paymentMethod.name").isEqualTo("Credit Card")
                .jsonPath("$[0].fidelityPoints").isEqualTo(100)
                .jsonPath("$[0].averagePurchase").isEqualTo(75.50)
                .jsonPath("$[1].name").isEqualTo("Jane")
                .jsonPath("$[1].lastName").isEqualTo("Smith")
                .jsonPath("$[1].address.street").isEqualTo("Green Valley Rd")
                .jsonPath("$[1].address.city").isEqualTo("Tallinn")
                .jsonPath("$[1].address.province").isEqualTo("Harju County")
                .jsonPath("$[1].address.postalCode").isEqualTo(56789)
                .jsonPath("$[1].address.country.name").isEqualTo("Estonia")
                .jsonPath("$[1].paymentMethod.name").isEqualTo("PayPal")
                .jsonPath("$[1].fidelityPoints").isEqualTo(150)
                .jsonPath("$[1].averagePurchase").isEqualTo(100.20);
    }

    @Test
    @DisplayName("Get User by ID IT")
    @Order(5)
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

    @Test
    @DisplayName("Get List of Users by Name IT")
    @Order(6)
    void getListOfUsersByNameTest() {
        String nameToFilter = "John";
        client.get().uri("/users/list?name=" + nameToFilter)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].name").isEqualTo("John")
                .jsonPath("$[0].lastName").isEqualTo("Doe")
                .jsonPath("$[0].address.street").isEqualTo("Sunset Blvd")
                .jsonPath("$[0].address.city").isEqualTo("Barcelona")
                .jsonPath("$[0].address.province").isEqualTo("Catalonia")
                .jsonPath("$[0].address.postalCode").isEqualTo(12345)
                .jsonPath("$[0].address.country.name").isEqualTo("Spain")
                .jsonPath("$[0].paymentMethod.name").isEqualTo("Credit Card")
                .jsonPath("$[0].fidelityPoints").isEqualTo(100)
                .jsonPath("$[0].averagePurchase").isEqualTo(75.50)
                .jsonPath("$[10].name").isEqualTo("John")
                .jsonPath("$[10].lastName").isEqualTo("Lennon")
                .jsonPath("$[10].address.street").isEqualTo("123 Main St")
                .jsonPath("$[10].address.city").isEqualTo("Liverpool")
                .jsonPath("$[10].address.province").isEqualTo("Merseyside")
                .jsonPath("$[10].address.postalCode").isEqualTo(13456)
                .jsonPath("$[10].address.country.name").isEqualTo("United Kingdom")
                .jsonPath("$[10].paymentMethod.name").isEqualTo("PayPal")
                .jsonPath("$[10].fidelityPoints").isEqualTo(170)
                .jsonPath("$[10].averagePurchase").isEqualTo(75.0)
                .jsonPath("$[99].name").doesNotExist();
    }

    // LOAD TEST

    @Test
    @DisplayName("Update User by ID Integration Test")
    @Order(8)
    void updateUserByIdTest() {
        long userIdToUpdate = 1L;
        UserRequest updatedUser = new UserRequest(
                "Carlos",
                "Gonzalez",
                "Industria",
                "Sabadell",
                "Barcelona",
                57362,
                "Spain",
                "Paypal",
                300,
                85.0
        );

        client.put().uri("/users/" + userIdToUpdate)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedUser)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Carlos")
                .jsonPath("$.lastName").isEqualTo("Gonzalez")
                .jsonPath("$.address.street").isEqualTo("Industria")
                .jsonPath("$.address.city").isEqualTo("Sabadell")
                .jsonPath("$.address.province").isEqualTo("Barcelona")
                .jsonPath("$.address.postalCode").isEqualTo(57362)
                .jsonPath("$.address.country.name").isEqualTo("Spain")
                .jsonPath("$.paymentMethod.name").isEqualTo("Paypal")
                .jsonPath("$.fidelityPoints").isEqualTo(300)
                .jsonPath("$.averagePurchase").isEqualTo(85.0);
    }
}